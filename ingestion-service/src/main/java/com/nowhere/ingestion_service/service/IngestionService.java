package com.nowhere.ingestion_service.service;

import com.nowhere.ingestion_service.dto.EnergyUsageDto;
import com.nowhere.ingestion_service.kafka.event.EnergyUsageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IngestionService {

    private final KafkaTemplate<String, EnergyUsageEvent> kafkaTemplate;
    private final String topic;

    public IngestionService(KafkaTemplate<String, EnergyUsageEvent> kafkaTemplate,
                            @Value("${spring.kafka.template.default-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void ingestEnergyUsage(EnergyUsageDto input) {
        EnergyUsageEvent event = EnergyUsageEvent.builder()
                .deviceId(input.deviceId())
                .energyConsumed(input.energyConsumed())
                .timestamp(input.timestamp())
                .build();

        // Key by device id so events for the same device keep their order
        kafkaTemplate.send(topic, String.valueOf(event.deviceId()), event);
        log.debug("Ingested Energy Usage Event: {}", event);
    }
}
