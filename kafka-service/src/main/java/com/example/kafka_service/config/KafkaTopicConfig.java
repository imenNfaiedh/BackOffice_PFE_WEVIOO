package com.example.kafka_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    //Cette méthode retourne un objet NewTopic qui représente un topic Kafka nommé "topicNum1".
    public NewTopic firstTopic() {
        return TopicBuilder
                .name("topicNum1")
                .partitions(5)
                .replicas(3) // Réplique chaque partition sur 3 broker
                .build();

    }}

