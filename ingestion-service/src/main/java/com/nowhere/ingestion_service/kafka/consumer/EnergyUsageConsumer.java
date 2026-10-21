package com.nowhere.ingestion_service.kafka.consumer;

import com.nowhere.ingestion_service.kafka.event.EnergyUsageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EnergyUsageConsumer {

    @KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "energy-tracker-group")
    public void consumeEnergyUsage(EnergyUsageEvent event) {
        log.info("Received Energy Usage Event from Kafka: deviceId={}, energyConsumed={}, timestamp={}",
                event.deviceId(), event.energyConsumed(), event.timestamp());
        // Process telemetry event, aggregate or check alerting thresholds
    }
}
