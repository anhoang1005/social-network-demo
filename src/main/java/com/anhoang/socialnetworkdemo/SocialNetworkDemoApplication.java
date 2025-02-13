package com.anhoang.socialnetworkdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SocialNetworkDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialNetworkDemoApplication.class, args);
    }

}