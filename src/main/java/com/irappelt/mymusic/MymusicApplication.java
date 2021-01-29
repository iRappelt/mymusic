package com.irappelt.mymusic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@MapperScan("com.irappelt.mymusic.mapper")
@EntityScan(basePackages={"com.irappelt.mymusic.model.po"})
@EnableJpaRepositories(basePackages = {"com.irappelt.mymusic.dao"})
public class MymusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(MymusicApplication.class, args);
    }

}
