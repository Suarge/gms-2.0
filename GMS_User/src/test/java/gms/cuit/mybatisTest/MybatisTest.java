package gms.cuit.mybatisTest;

import gms.cuit.entity.*;
import gms.cuit.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

@SpringBootTest
public class MybatisTest {
    @Autowired
    UserMapper userMapper;
    @Test
    void TestMyBatis(){
        System.out.println(userMapper.finAllVenueByTypeAndDate("篮球场","2020-04-08"));
    }

    @Test
    void doLogin(){
        Gms_User gms_user = userMapper.doLogin("2017051111", "123456");
        System.out.println(gms_user);
    }
    @Test
    void vdstate(){
        Gms_Vdstate vdstate = userMapper.findVdstate("19", "2020-04-08");
        System.out.println(vdstate);
    }
    @Test
    void saveVdstate(){

        userMapper.saveVdstate("00000000000012222220000","19","2020-04-08");
    }
    @Test
    void getOrderState(){
        Gms_Notice noticeById = userMapper.getNoticeById("0279046b3a3a446f8fe2c42dac7b1468");
        System.out.println(noticeById);
    }
    @Test
    void regidter(){
        Gms_User gms_user = new Gms_User("2017051111","123456","男",12,"1274334685@qq.com");

        Integer i = userMapper.register(gms_user);
        System.out.println(i);
    }
}
