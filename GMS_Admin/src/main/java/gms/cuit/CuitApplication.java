package gms.cuit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = "gms.cuit.mapper")
public class CuitApplication {

    public static void main(String[] args) {
        SpringApplication.run(CuitApplication.class, args);
    }

}
