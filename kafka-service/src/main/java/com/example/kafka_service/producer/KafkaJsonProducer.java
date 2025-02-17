package com.example.kafka_service.producer;

import com.example.kafka_service.payload.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class KafkaJsonProducer {

    private final KafkaTemplate<String, Student> kafkaTemplate;
    @Autowired
    public KafkaJsonProducer(KafkaTemplate<String, Student> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(Student student) {

        Message<Student> message = MessageBuilder
                .withPayload(student)
                .setHeader(KafkaHeaders.TOPIC, "topicNum1")
                .build();

        kafkaTemplate.send(message);
    }
}
