package com.example.transaction_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = Paths.get("C:/Users/Lenovo/Desktop/stagePfe/BackOffice_PFE/pfaMicros/Fraud-Detection-service/images")
                .toAbsolutePath()
                .normalize()
                .toString() + "/";

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadDir);
    }
}
