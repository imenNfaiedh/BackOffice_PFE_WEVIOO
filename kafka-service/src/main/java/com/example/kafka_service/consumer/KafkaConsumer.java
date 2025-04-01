package com.example.kafka_service.consumer;


import com.example.kafka_service.payload.Student;
import com.example.kafka_service.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
//@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "any-topic-name", groupId = "myGrp")
    public void consumeMessage(String message) {
        System.out.println("Received message: " + message);
    }
}
