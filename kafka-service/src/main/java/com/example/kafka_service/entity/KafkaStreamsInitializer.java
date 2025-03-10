package com.example.kafka_service.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamsInitializer implements CommandLineRunner {

    private final TransactionEnrichmentProcessor processor;

    @Autowired
    public KafkaStreamsInitializer(TransactionEnrichmentProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void run(String... args) throws Exception {
        processor.start();
    }
}
