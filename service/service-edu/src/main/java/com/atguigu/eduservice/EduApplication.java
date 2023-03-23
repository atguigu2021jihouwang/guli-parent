package com.atguigu.eduservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 在调用端的启动类添加注解 例如:service-edu 调用 service-vod 就需要在 service-edu模块上天上注解
 */
@EnableFeignClients
@SpringBootApplication
//nacos注册
@EnableDiscoveryClient
//扫描包
@ComponentScan(basePackages = {"com.atguigu"})
public class EduApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);
    }
}
