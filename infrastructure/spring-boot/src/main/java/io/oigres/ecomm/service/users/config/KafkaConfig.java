package io.oigres.ecomm.service.users.config;

import io.oigres.ecomm.service.users.domain.TransactionalOutbox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<String, TransactionalOutbox> messageKafkaTemplate(
            ProducerFactory<String, TransactionalOutbox> messageProducerFactory,
            @Value("${ecomm.service.users.topics.entities-update}") String entitiesUpdateTopicName
    ) {
        KafkaTemplate<String, TransactionalOutbox> template = new KafkaTemplate<>(messageProducerFactory);
        template.setDefaultTopic(entitiesUpdateTopicName);
        template.setObservationEnabled(true);
        return template;
    }

}