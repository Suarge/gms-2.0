package gms.cuit;

import gms.cuit.entity.Gms_Order;
import gms.cuit.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CuitApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        List<Gms_Order> productByPage = userMapper.findProductByPage("2017051144", 1, 5, "");
        System.out.println(productByPage);
    }
    @Test
    void testcount() {
        Integer productByPage = userMapper.getCount("2017051144"," ");

        System.out.println(productByPage);
    }

}
