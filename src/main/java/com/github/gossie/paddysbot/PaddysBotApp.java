package com.github.gossie.paddysbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class PaddysBotApp {

    public static void main(String[] args) {
        SpringApplication.run(PaddysBotApp.class, args);
    }

}
