package gms.cuit.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminMapperTest {
    @Autowired
    AdminMapper adminMapper;
    @Test
    void TestMyBatis(){
        System.out.println(adminMapper.login("admin","e10adc3949ba59abbe56e057f20f883e"));
    }
    @Test
    void TestUpdatePassword(){
        adminMapper.updatePassword("admin","123456");
    }
}
