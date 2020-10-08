package com.xem_vn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class XemVnApplication {

    public static void main(String[] args) {
        SpringApplication.run(XemVnApplication.class, args);
    }

}
