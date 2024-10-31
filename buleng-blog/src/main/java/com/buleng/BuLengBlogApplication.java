package com.buleng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.buleng.mapper")
public class BuLengBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BuLengBlogApplication.class,args);
    }
}
