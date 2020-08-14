package gms.cuit.service;

import gms.cuit.entity.*;
import gms.cuit.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService implements UserDetailsService {

    @Autowired
    AdminMapper adminMapper;

    /*
    * 实现该方法来获取对应用户
    * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username==null){
            throw new UsernameNotFoundException("用户名不存在!");
        }
        Gms_Admin admin = adminMapper.loadUserByUsername(username);
        if(admin==null){
            throw new UsernameNotFoundException("用户名不存在!");
        }
        //这里本应该进行角色获取,但是由于表的特殊情况不需要
        return admin;
    }

    public void updatePassword(String username,String newpassword){
        adminMapper.updatePassword(username,newpassword);
    }

    public Double getSummaryTodayOrderProfit(String currentDate) {
        String date_st = currentDate+" 00:00:00";
        String date_ed = currentDate+" 23:00:00";
        return adminMapper.getSummaryTodayOrderProfit(date_st,date_ed);
    }

    public Integer getSummaryTodayOrderCount(String currentDate) {
        String date_st = currentDate+" 00:00:00";
        String date_ed = currentDate+" 23:00:00";
        return adminMapper.getSummaryTodayOrderCount(date_st,date_ed);
    }

    public String getSummaryOrderCountByDateAndVen(String currentDate) {
        String date_st = currentDate+" 00:00:00";
        String date_ed = currentDate+" 23:00:00";
        List<Object> catlist = adminMapper.queryVenueType(); //查询出所有的类别
        String valString = "";
        for(Object catitem : catlist) { //遍历每一个类别
            //获取类别名字
            String catitemname = catitem.toString();
            //获取订单场馆分布
            int count = adminMapper.getOrderCountByDateAndVen(date_st,date_ed,catitemname);
            if(catitemname.equals(catlist.get(0)) == false) valString += " ";
            valString += ""+count;
        }
        return valString;
    }

    public List<Gms_Order> getLatestOrderList(int get_count) {
        return adminMapper.getLatestOrderList(get_count);
    }

    public PageBean<Gms_Venue> queryVenueByKey(int currentPage, int currentCount, String query_key) {
        PageBean pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentCount(currentCount);
        int totalCount = adminMapper.getVenueTotalCountByKeyQuery(query_key);
        pageBean.setTotalCount(totalCount);
        int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
        pageBean.setTotalPage(totalPage);
        int index = (currentPage-1)*currentCount;
        List<Gms_Venue> venue = adminMapper.queryVenueByKey(index,currentCount,query_key);
        pageBean.setList(venue);
        return pageBean;
    }

    public void addVenue(Gms_Venue gms_venue) {
        adminMapper.addVenue(gms_venue);
    }

    public void updateVenue(Gms_Venue gms_venue) {
        adminMapper.updateVenue(gms_venue);
    }

    public void delVenue(Gms_Venue gms_venue) {
        adminMapper.delVenue(gms_venue);
    }

    public int getOrderTotalCountByKeyQuery(String query_key) {
        return adminMapper.getOrderTotalCountByKeyQuery(query_key);
    }

    public List<Gms_Order> queryOrderByKey(int index, int currentCount, String query_key, String sortStatus) {
        return adminMapper.queryOrderByKey(index,currentCount,query_key,sortStatus);
    }

    public PageBean<Gms_Notice> queryNoticeByKey(int currentPage, int currentCount, String query_key) {
        PageBean pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentCount(currentCount);
        int totalCount = adminMapper.getNoticeTotalCountByKeyQuery(query_key);
        pageBean.setTotalCount(totalCount);
        int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
        pageBean.setTotalPage(totalPage);
        int index = (currentPage-1)*currentCount;
        List<Gms_Notice> notice_list = adminMapper.queryNoticeByKey(index,currentCount,query_key);
        pageBean.setList(notice_list);
        return pageBean;
    }

    public void addNotice(Gms_Notice gms_notice) {
        adminMapper.addNotice(gms_notice);
    }

    public void updateNotice(Gms_Notice gms_notice) {
        adminMapper.updateNotice(gms_notice);
    }

    public void delNotice(Gms_Notice gms_notice) {
        adminMapper.delNotice(gms_notice);
    }

    public List<Gms_Bck_analytics> getAnalyticsByDateAndVen(String date_st, String date_ed) {
        List<Gms_Bck_analytics> analyticslist = new ArrayList<Gms_Bck_analytics>();
        //查询出所有的场馆
        List<Gms_Venue> venlist = adminMapper.queryVenueByKey(0,999999999,"");
        for(Gms_Venue gms_venue : venlist) { //遍历每一个场馆
            //获取场馆名字
            String ven_name = gms_venue.getVenue_Name();
            Gms_Bck_analytics gms_Bck_analytics = new Gms_Bck_analytics();
            gms_Bck_analytics.setAnalytics_Name(ven_name);
            //获取订单收益
            Double analytics_Profit = adminMapper.getOrderProfitByDateAndVenOrCat(date_st, date_ed, ven_name,null);
            gms_Bck_analytics.setAnalytics_Profit(analytics_Profit);
            //获取租用人数
            Integer analytics_Rent = adminMapper.getOrderCountByDateAndVenOrCatAndSexAndAge(date_st, date_ed, ven_name,null,null,null);
            gms_Bck_analytics.setAnalytics_Rent(analytics_Rent);
            //获取总数
            int totalcount = adminMapper.getOrderCountByDateAndVenOrCatAndSexAndAge(date_st, date_ed,null,null,null,null);
            int mycount = analytics_Rent;
            //获取使用率
            double analytics_Usage = 0.00;
            if(totalcount!=0) analytics_Usage = (double)mycount*1.0/((double)totalcount*1.0);
            NumberFormat nf = NumberFormat.getPercentInstance();/*设置为百分比*/
            nf.setMinimumFractionDigits(2);//设置该百分比数字，保留2位小数;
            nf.setRoundingMode(RoundingMode.HALF_UP); //设置满5向上进位，即四舍五入;
            gms_Bck_analytics.setAnalytics_Usage(nf.format(analytics_Usage));
            //获取男女欢迎度
            int malecount = adminMapper.getOrderCountByDateAndVenOrCatAndSexAndAge(date_st, date_ed, ven_name,null, "男",null);
            int femalecount = adminMapper.getOrderCountByDateAndVenOrCatAndSexAndAge(date_st, date_ed, ven_name,null, "女",null);
            gms_Bck_analytics.setAnalytics_Sexpercentage(malecount+" "+femalecount);
            //获取订单年龄分布
            String analytics_Agepercentage = "";
            for(int age=10;age<=60;age+=10) {
                analytics_Agepercentage+=adminMapper.getOrderCountByDateAndVenOrCatAndSexAndAge(date_st, date_ed, ven_name,null,null, age+"");
                if(age!=60) analytics_Agepercentage+=" ";
            }
            gms_Bck_analytics.setAnalytics_Agepercentage(analytics_Agepercentage);
            analyticslist.add(gms_Bck_analytics);
        }
        return analyticslist;
    }

    public List<Gms_Bck_analytics> getAnalyticsByDateAndCat(String date_st, String date_ed) {
        List<Gms_Bck_analytics> analyticslist = new ArrayList<Gms_Bck_analytics>();
        //查询出所有的类别
        List<Object> venlist = adminMapper.queryVenueType();
        for(Object tmppp : venlist) { //遍历每一个类别
            //获取场馆名字
            String cat_name = tmppp.toString();
            Gms_Bck_analytics gms_Bck_analytics = new Gms_Bck_analytics();
            gms_Bck_analytics.setAnalytics_Name(cat_name);
            //获取订单收益
            Double analytics_Profit = adminMapper.getOrderProfitByDateAndVenOrCat(date_st, date_ed,null, cat_name);
            gms_Bck_analytics.setAnalytics_Profit(analytics_Profit);
            //获取租用人数
            Integer analytics_Rent = adminMapper.getOrderCountByDateAndVenOrCatAndSexAndAge(date_st, date_ed, null,cat_name,null,null);
            gms_Bck_analytics.setAnalytics_Rent(analytics_Rent);
            //获取总数
            int totalcount = adminMapper.getOrderCountByDateAndVenOrCatAndSexAndAge(date_st, date_ed,null,null,null,null);
            int mycount = analytics_Rent;
            //获取使用率
            double analytics_Usage = 0.00;
            if(totalcount!=0) analytics_Usage = (double)mycount*1.0/((double)totalcount*1.0);
            NumberFormat nf = NumberFormat.getPercentInstance();/*设置为百分比*/
            nf.setMinimumFractionDigits(2);//设置该百分比数字，保留2位小数;
            nf.setRoundingMode(RoundingMode.HALF_UP); //设置满5向上进位，即四舍五入;
            gms_Bck_analytics.setAnalytics_Usage(nf.format(analytics_Usage));
            //获取男女欢迎度
            int malecount = adminMapper.getOrderCountByDateAndVenOrCatAndSexAndAge(date_st, date_ed,null,cat_name, "男",null);
            int femalecount = adminMapper.getOrderCountByDateAndVenOrCatAndSexAndAge(date_st, date_ed,null,cat_name, "女",null);
            gms_Bck_analytics.setAnalytics_Sexpercentage(malecount+" "+femalecount);
            //获取订单年龄分布
            String analytics_Agepercentage = "";
            for(int age=10;age<=60;age+=10) {
                analytics_Agepercentage+=adminMapper.getOrderCountByDateAndVenOrCatAndSexAndAge(date_st, date_ed,null,cat_name,null, age+"");
                if(age!=60) analytics_Agepercentage+=" ";
            }
            gms_Bck_analytics.setAnalytics_Agepercentage(analytics_Agepercentage);
            analyticslist.add(gms_Bck_analytics);
        }
        return analyticslist;
    }

    public List<Gms_Venue> queryAllVenue() {
        return adminMapper.queryVenueByKey(0,99999999,"");
    }

    public List<Gms_Notice> queryAllNotice() {
        return adminMapper.queryNoticeByKey(0,99999999,"");
    }

    public Integer updateOrderStateByTime(String date, String hour) {
        return adminMapper.updateOrderStateByTime(date,hour);
    }

    public List<Gms_Vdstate> queryOrderStateByTime(String date) {
        return adminMapper.queryOrderStateByTime(date);
    }

    public void updateVdState(Gms_Vdstate vdstate) {
        adminMapper.updateVdState(vdstate);
    }

}
