package com.plasticene.fast;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.plasticene.fast.dao")
public class FastApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastApiServiceApplication.class, args);
    }

}
