package gms.cuit.service;

import gms.cuit.entity.*;
import gms.cuit.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class UserService {

    @Autowired
    private MailService mailService;

    @Autowired
    private Environment env;

    @Autowired
    private  UserMapper userMapper;

    @Autowired
    private RabbitSenderService rabbitSenderService;

    private static final ReentrantLock lock = new ReentrantLock();


    /*
     * 首页部分
     */
    public List<Gms_Notice> findAllNotice(){
        return userMapper.findAllNotice();
    }

    public List<Gms_Type> findAllType(){
        return userMapper.findAllType();
    }

    public List<Gms_Vdstate> finAllVenueByTypeAndDate(String venueName, String currentDate){
        return userMapper.finAllVenueByTypeAndDate(venueName,currentDate);
    }

    public Gms_Venue findVenueById(String id){
        return userMapper.findVenueById(id);
    }

    public Gms_Notice getNoticeById(String noticeId){
        return userMapper.getNoticeById(noticeId);
    }
    /*
     * 加同步锁判断是否订单生成成功  加事务保证写入订单和修改状态同时执行
     */
    @Transactional
    public boolean isAddOrderSuccess(Gms_Order order){
        Boolean flag = false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String  currentDate =  format.format( order.getOrder_Date());

        //判断当前场馆状态是否是可预约状态
        Integer orderState = userMapper.getOrderState(order.getOrder_Venue().getVenue_Id(), currentDate, order.getOrder_St(), order.getOrder_Ed());
        //首先状态只有三种，sql语句排出了已取消的状态，
        //其次，已经完成的状态肯定在超过这个时间段后面，所以如果可以预约就不会查出已完成的状态
        //最后，这个订单查出来只能为0(已被预约)或者为空，还没有被预约
        if(orderState != null){
            return false;
        }

        //如果可预约，表示一部分线程通过上面的限制，加锁
        lock.lock();
        try{
            ///双重检验锁，因为前面只检查了一部分线程
            //判断当前场馆状态是否是可预约状态
            Integer orderState1 = userMapper.getOrderState(order.getOrder_Venue().getVenue_Id(), currentDate, order.getOrder_St(), order.getOrder_Ed());
            //如果当前这个场馆的订单已经生成
            if(orderState1 != null){
                return false;
            }

            int res = userMapper.addOrder(order);
            //表示预约成功
            if(res > 0){
                //1.修改数据库库状态 可以抽取出来这里不抽取
                Gms_Vdstate vdstate = userMapper.findVdstate(order.getOrder_Venue().getVenue_Id(), currentDate);
                //获取开始结束时间
                Integer order_st = order.getOrder_St();
                String vdstate_st = vdstate.getVdstate_St();

                char[] ch = vdstate_st.toCharArray();
                for(int i=0;i<ch.length;i++) {
                    if(i==order_st) {
                        //将当前状态改为黄色，表示已预约
                        ch[i]='2';
                    }
                }
                String new_vdstate_st = String.valueOf(ch);
                //vdstate.setVdstate_St(new_vdstate_st);
                //保存分时状态
                Integer saveVdstate = userMapper.saveVdstate(new_vdstate_st,order.getOrder_Venue().getVenue_Id(), currentDate);
                if(saveVdstate > 0){
                    flag = true;
                }
            }else{
                //订单生成失败，直接返回
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("订单生成异常");
        }finally {
            lock.unlock();
        }

        //如果能执行到这里，说明当前用户已经生成了订单
        //发送邮件  高并发可以通过消息队列获取这个dto，然后调用消息队列发送邮件，这里直接发送
//        final String content = String.format(env.getProperty("mail.kill.item.success.content"),order.getOrder_Venue().getVenue_Name(),currentDate+" "+order.getOrder_St()+":00-"+order.getOrder_Ed()+":00");
//        MailDto dto = new MailDto(env.getProperty("mail.kill.item.success.subject"),content,new String[]{order.getOrder_User().getUser_Email()});
//        //调用邮件服务
//        mailService.sendHTMLMail(dto);

        //异步发送邮件
        rabbitSenderService.sendOrderSuccessEmailMsg(order);

        return flag;
    }



    /*
     * 个人中心部分
     */
    public PageBean findOrderListByUserId(String user_Id,int currentPage, int currentCount,String query_key){
        PageBean<Gms_Order> pageBean = new PageBean<Gms_Order>();
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentCount(currentCount);
        int totalCount = userMapper.getCount(user_Id,query_key);
        pageBean.setTotalCount(totalCount);
        int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
        pageBean.setTotalPage(totalPage);
        //5.封装当前页显示的数据
        int index = (currentPage-1)*currentCount;
        //将list<map<>>遍历封装到orderlist
        List<Gms_Order> list = userMapper.findProductByPage(user_Id, index, currentCount, query_key);
        pageBean.setList(list);
        return pageBean;
    }

    public void delOrderById(String id){
        userMapper.delOrderById(id);
    }

    public Gms_Vdstate findVdstateById(String venueid,String date){
        return userMapper.findVdstate(venueid,date);
    }

    public void saveVdstate(String new_vdstate_st,String venueid,String currentDate){
        userMapper.saveVdstate(new_vdstate_st,venueid,currentDate);
    }

    /*
     * 登录注册部分
     */
    public Gms_User doLogin(String id,String password){
        return userMapper.doLogin(id,password);
    }

    public void updatePassword(String user_id,String newpassword){
        userMapper.updatePassword(user_id,newpassword);
    }

    //注册加邮件发送
    public Integer register(Gms_User gms_user){
        Integer res = userMapper.register(gms_user);
        if(res > 0){
            rabbitSenderService.sendRegisterSuccessEmailMsg(gms_user);
        }
        return res;
    }

    //测试jmeter
    public Gms_User finUserById(String id){
        return userMapper.finUserById(id);
    }

}
