package com.rolin.orangesmart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@MapperScan("com.rolin.orangesmart.mapper")
@SpringBootApplication
@EnableAspectJAutoProxy
public class OrangeSmartApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrangeSmartApplication.class, args);
    }

}
