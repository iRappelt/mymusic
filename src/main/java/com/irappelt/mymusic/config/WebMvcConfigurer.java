package com.irappelt.mymusic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author huaiyu
 * @project: mymusic
 * @description TODO
 * @date 2021/4/14 17:54
 **/
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOriginPatterns("*")
                .allowCredentials(true).allowedMethods("*").maxAge(3600);
    }
}
