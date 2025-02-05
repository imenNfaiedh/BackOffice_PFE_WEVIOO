package com.example.config_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")

public class ConfigController {

    @GetMapping("/get")
    public String home() {
        return "<h2>Microservice Config is running</h2>";
    }
}
