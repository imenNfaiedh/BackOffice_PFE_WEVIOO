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

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

//    // @KafkaListener(topics = "topicNum1", groupId = "myGrp")
//    public void consumeMsg(String msg) {
//
//         log.info(format("Consuming the message from topicNum1 Topic:: %s", msg));
//    }

    @KafkaListener(topics = "topicNum1", groupId = "myGrp")
    public void consumeJsonMsg(Student student) {
        log.info(format("Consuming the message from  Topic:: %s", student.toString()));
    }
}
