package gms.cuit.controller;

import gms.cuit.entity.*;
import gms.cuit.service.UsernamePasswordUtilsService;
import gms.cuit.service.UserService;
import gms.cuit.utils.ExportExcelUtils;
import gms.cuit.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private SnowFlake snowFlake = new SnowFlake(2,3);

    @Autowired
    private UserService userService;

    @Autowired
    private UsernamePasswordUtilsService usernamePasswordUtilsService;

    /*
     * 查询使用get, 其它使用post
     * 首页部分
     */

    @GetMapping("/findAllNotice")
    public List<Gms_Notice> findAllNotice(){
        return userService.findAllNotice();
    }

    @GetMapping("/findVenueType")
    public List<Gms_Type> findVenueType(){
        return userService.findAllType();
    }

    @GetMapping("/findVenueList")
    public List<Gms_Vdstate> findVenueList(@RequestParam Map<String,String> map){
        String currentDate = map.get("currentDate");
        String venueName = map.get("venueName");
        return userService.finAllVenueByTypeAndDate(venueName, currentDate);
    }

    @PostMapping("/getNotice")
    public Gms_Notice getNotice(@RequestBody Map<String,String> map){
        String noticeId = map.get("noticeId");
        return userService.getNoticeById(noticeId);
    }

    @PostMapping("/confirmOrder")
    public Map<String, String> confirmOrder(@RequestBody Gms_Order order, @RequestParam String venue_Id){
        Map<String,String> map = new HashMap<>();
        //封装订单号
        order.setOrder_Id(String.valueOf(snowFlake.nextId()));
        //封装场馆
        Gms_Venue venueById = userService.findVenueById(venue_Id);
        order.setOrder_Venue(venueById);
        //封装用户user
        Gms_User user = usernamePasswordUtilsService.getSession();
        //Gms_User user = userService.finUserById(order_User);
        order.setOrder_User(user);
        //封装订单状态
        order.setOrder_State(0);
        //封装订单生成时间
        order.setOrder_Mktime(new Date());
        //提交订单
        boolean res = userService.isAddOrderSuccess(order);
        if(res){
            //表示预约成功
            map.put("info","ok");
        }else{
            //表示预约失败
            map.put("info","error");
        }
        return map;
    }

    /*
     * 个人中心部分
     */
    @PostMapping("/getOrder")
    public Map<String,Object> getOrder(@RequestBody Map<String,String> reqmap){
        Map<String,Object> map = new HashMap<>();
        //获取模糊查询
        String query_key = reqmap.get("query_key");
        if(query_key==null) query_key="";
        if(query_key!=null&&query_key.trim().equals("")==false) query_key=query_key.trim();
        //获取当前对象的id
        Gms_User session = usernamePasswordUtilsService.getSession();
        String user_Id = session.getUser_Id();
        //设置当前页和当前显示条数
        String currentPageStr = reqmap.get("currentPage");
        if (currentPageStr == null||currentPageStr.equals("")) currentPageStr = "1";
        int currentPage = Integer.parseInt(currentPageStr);
        int currentCount = 6;

        PageBean pageBean = userService.findOrderListByUserId(user_Id,currentPage,currentCount,query_key);
        map.put("query_key", query_key);
        map.put("currentPage",pageBean.getCurrentPage());
        map.put("currentCount",pageBean.getCurrentCount());
        map.put("totalCount",pageBean.getTotalCount());
        map.put("totalPage",pageBean.getTotalPage());
        map.put("list",pageBean.getList());
        return map;
    }

    /*
     * 这里先更改的订单状态，后面又去修改场馆状态，必须同时执行或者不执行，
     * 加事务控制
     */
    @PostMapping("/delOrder")
    @Transactional
    public Map<String,String> delOrder(@RequestBody Map<String,String> reqmap){
        Map<String,String> map = new HashMap<>();
        String orderid = reqmap.get("orderId");//订单号
        String venueid = reqmap.get("venueId");//订单场馆id
        String orderdate = reqmap.get("orderDate");//订单日期
        String st = reqmap.get("orderSt");//分时状态第一个
        userService.delOrderById(orderid);//订单改变状态

        Gms_Vdstate vdstate = userService.findVdstateById(venueid,orderdate);

        //获得开始时间order_St
        int order_st = Integer.parseInt(st);
        //获得分时状态
        String vdstate_st = vdstate.getVdstate_St();

        char[] ch = vdstate_st.toCharArray();
        for(int i=0;i<ch.length;i++) {
            if(i==order_st) {
                //将当前状态改为白色
                ch[i]='1';
            }
        }

        String new_vdstate_st = String.valueOf(ch);
        userService.saveVdstate(new_vdstate_st,venueid,orderdate);//保存修改
        map.put("info","ok");
        return map;
    }

    @GetMapping("/export_order")
    public void export_order(HttpServletResponse response) throws IOException {
        Map<String, String> title = new HashMap(); // 表头
        List<Map<String, Object>> data = new ArrayList(); // 需要导出的数据
        Map<String, Integer> position = new HashMap(); // 表头字段对应的位置（自定义位置）

        //获取当前对象的id
        Gms_User user = usernamePasswordUtilsService.getSession();
        String user_Id = user.getUser_Id();

        //根据user_id查询所有订单
        PageBean pageBean = userService.findOrderListByUserId(user_Id, 1, 999999, "");
        List<Gms_Order> orderlist = pageBean.getList();

        // 设置表头字段位置
        position.put("Venue_Name", 0);
        position.put("Venue_Type", 1);
        position.put("Order_Date", 2);
        position.put("Order_St", 3);
        position.put("Order_Ed", 4);
        position.put("Order_Price", 5);
        position.put("Order_Mktime", 6);
        position.put("Order_Sate", 7);

        // 设置表头信息
        title.put("Venue_Name", "预约场地");
        title.put("Venue_Type", "场地类别");
        title.put("Order_Date", "预约日期");
        title.put("Order_St", "开始时间");
        title.put("Order_Ed", "结束时间");
        title.put("Order_Price", "预约价格");
        title.put("Order_Mktime", "预约生成时间");
        title.put("Order_Sate", "预约状态");

        Map<String, Object> orderMap = null;

        for (Gms_Order order : orderlist) {
            orderMap = new HashMap();
            orderMap.put("Venue_Name", order.getOrder_Venue().getVenue_Name());
            orderMap.put("Venue_Type", order.getOrder_Venue().getVenue_Type());
            orderMap.put("Order_Date", order.getOrder_Date());
            orderMap.put("Order_St", order.getOrder_St()+":00");
            orderMap.put("Order_Ed", order.getOrder_Ed()+":00");
            orderMap.put("Order_Price", order.getOrder_Price());
            orderMap.put("Order_Mktime", order.getOrder_Mktime());
            String Order_Sate = "";
            if(order.getOrder_State()==0) Order_Sate="进行中";
            else if(order.getOrder_State()==1) Order_Sate="已取消";
            else if(order.getOrder_State()==2) Order_Sate="已完成";
            orderMap.put("Order_Sate", Order_Sate);
            data.add(orderMap);     // 将userMap添加到List集合中
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String date = df.format(new java.util.Date());
        String excelName = "订单列表-" + date + ".xlsx";
        String sheetName = "订单列表数据";
        excelName = URLEncoder.encode(excelName, "UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + excelName);
        response.setContentType("application/x-download");
        // 调用写好的工具类的导出数据方法   传入对应的参数
        ExportExcelUtils.exportDataToExcel(title, position, data, sheetName, response.getOutputStream());

    }


    /*
     * 登录注册部分
     */
    @GetMapping("/getSession")
    public Gms_User getSession(){
        return usernamePasswordUtilsService.getSession();
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestBody Map<String,String> map){
        String user_id = map.get("username");
        String newpassword = map.get("newpassword");
        userService.updatePassword(user_id,newpassword);
        return "{}";
    }

    @GetMapping("/checkUsername")
    public Map<String,Object> checkUsername(@RequestParam Map<String,String> resmap){
        Map<String,Object> map = new HashMap<>();
        Gms_User user = userService.finUserById(resmap.get("user_Id"));
        if(user != null){
            map.put("info",user);
        }else {
            map.put("info",null);
        }
        return map;
    }

    @PostMapping("/register")
    public Map<String,String> register(@RequestBody Gms_User gms_user){
        Map<String,String> map = new HashMap<>();
        Integer res = userService.register(gms_user);
        if(res > 0){
            map.put("state","1");
            map.put("info","用户注册成功");
        }else {
            map.put("state","0");
            map.put("info","用户注册失败");
        }
        return map;
    }

}
