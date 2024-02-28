package com.devs.powerfit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PowerFitApplication {

    public static void main(String[] args) {
        SpringApplication.run(PowerFitApplication.class, args);
    }

}
