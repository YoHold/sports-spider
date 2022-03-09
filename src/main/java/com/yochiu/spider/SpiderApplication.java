package com.yochiu.spider;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpiderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        try {
            SpringApplication application = new SpringApplication(SpiderApplication.class);
            application.setWebApplicationType(WebApplicationType.NONE);
            application.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws InterruptedException {
        Thread.currentThread().join();
    }
}
