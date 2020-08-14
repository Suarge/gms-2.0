package gms.cuit.mapper;


import gms.cuit.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    List<Gms_Notice> findAllNotice();

    List<Gms_Type> findAllType();

    List<Gms_Vdstate> finAllVenueByTypeAndDate(String venueName,String currentDate);

    Gms_Venue findVenueById(String id);

    int addOrder(Gms_Order order);

    Integer getOrderState(String Venue_Id, String date,Integer st,Integer ed);

    Gms_Vdstate findVdstate(String venue_id, String date);

    Integer saveVdstate(@Param("vdstate") String vdstate, @Param("venue_Id")String venue_Id, @Param("date") String date);

    Gms_Notice getNoticeById(String noticeId);

    //个人中心部分
    Integer getCount(String user_Id,String query_key);

    List<Gms_Order> findProductByPage(String user_Id,int index,int currentCount,String query_key);

    void delOrderById(String id);

    //登录部分
    Gms_User doLogin(String id,String password);

    Gms_User finUserById(String id);

    void updatePassword(String user_id,String newpassword);

    Integer register(Gms_User gms_user);
}
