package com.xem_vn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SocialNetwork {

    public static void main(String[] args) {
        SpringApplication.run(SocialNetwork.class, args);
    }

}
