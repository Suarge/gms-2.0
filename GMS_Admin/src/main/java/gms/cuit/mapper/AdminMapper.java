package gms.cuit.mapper;


import gms.cuit.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper {
    public Gms_Admin login(String username, String password);

    //spring security进行数据库认证所需方法
    Gms_Admin loadUserByUsername(String username);

    void updatePassword(String username,String newpassword);

    Double getSummaryTodayOrderProfit(String date_st,String date_ed);

    Integer getSummaryTodayOrderCount(String date_st, String date_ed);

    List<Object> queryVenueType();

    Integer getOrderCountByDateAndVen(String date_st, String date_ed, String catitemname);

    List<Gms_Order> getLatestOrderList(int get_count);

    Integer getVenueTotalCountByKeyQuery(String query_key);

    List<Gms_Venue> queryVenueByKey(int index, int currentCount, String query_key);

    void addVenue(Gms_Venue gms_venue);

    void updateVenue(Gms_Venue gms_venue);

    void delVenue(Gms_Venue gms_venue);

    Integer getOrderTotalCountByKeyQuery(String query_key);

    List<Gms_Order> queryOrderByKey(int index, int currentCount, String query_key, String sortStatus);

    Integer getNoticeTotalCountByKeyQuery(String query_key);

    List<Gms_Notice> queryNoticeByKey(int index, int currentCount, String query_key);

    void addNotice(Gms_Notice gms_notice);

    void updateNotice(Gms_Notice gms_notice);

    void delNotice(Gms_Notice gms_notice);

    Double getOrderProfitByDateAndVenOrCat(String date_st, String date_ed, String ven_name,String cat_name);

    Integer getOrderRentByDateAndVen(String date_st, String date_ed, String ven_name);

    Integer getOrderTotalCountByDate(String date_st, String date_ed);

    Integer getOrderCountByDateAndVenOrCatAndSexAndAge(String date_st, String date_ed,String ven_name,String cat_name,String sex,String age);

    Integer updateOrderStateByTime(String date, String hour);

    List<Gms_Vdstate> queryOrderStateByTime(String date);

    void updateVdState(Gms_Vdstate vdstate);
}
