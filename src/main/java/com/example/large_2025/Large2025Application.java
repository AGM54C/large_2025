package com.example.large_2025;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.large_2025.pojo")  // 明确指定实体类包
@EnableJpaRepositories("com.example.large_2025.repository")  // 如果有repository
public class Large2025Application {
    public static void main(String[] args) {
        SpringApplication.run(Large2025Application.class, args);
    }
}