package com.example.befirst2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BeFirst2Application {

    public static void main(String[] args) {
        SpringApplication.run(BeFirst2Application.class, args).getBean(BeFirst2Application.class);
    }


}
