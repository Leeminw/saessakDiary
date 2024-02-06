package com.ssafy.saessak.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
//                .allowedOrigins("http://localhost:5173",
//                "http://i10a706.p.ssafy.io/",
//                "https://i10a706.p.ssafy.io/",
//                "http://i10a706.p.ssafy.io:5173");
    }
}