package com.example.transaction_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/convert")
    public ResponseEntity<?> convert(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double amount
    ) {
        String accessKey = "f7051dd0f5f927715e5cebf324590c46";
        String url = String.format("https://api.exchangerate.host/convert?access_key=%s&from=%s&to=%s&amount=%s",
                accessKey, from, to, amount);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur appel API externe : " + e.getMessage());
        }
    }

    @GetMapping("/timeseries")
    public ResponseEntity<String> getTimeSeries(
            @RequestParam("base") String base,
            @RequestParam("symbols") String symbols,
            @RequestParam("start_date") String startDate,
            @RequestParam("end_date") String endDate) {

        String url = String.format("https://api.exchangerate.host/timeseries?start_date=%s&end_date=%s&base=%s&symbols=%s",
                startDate, endDate, base, symbols);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok(response);
    }



}

