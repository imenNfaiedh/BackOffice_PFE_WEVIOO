package com.example.transaction_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = Paths.get("C:/Users/Lenovo/Desktop/stagePfe/BackOffice_PFE/pfaMicros/uploads/images").toAbsolutePath().normalize().toUri().toString();

        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations(uploadDir);
    }
}
