package com.nowhere.ingestion_service.simulation;

import com.nowhere.ingestion_service.dto.EnergyUsageDto;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Multi-threaded load generator. Enabled with simulation.parallel-enabled=true
 * (only one simulator should run at a time). Each batch blocks until every request
 * has completed, so batches can never pile up across schedule ticks.
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "simulation", name = "parallel-enabled", havingValue = "true")
public class ParallelDataSimulator implements CommandLineRunner {

    private final RestTemplate restTemplate;
    private final ExecutorService executorService;

    @Value("${simulation.parallel-threads}")
    private int parallelThreads;

    @Value("${simulation.requests-per-interval}")
    private int requestsPerInterval;

    @Value("${simulation.endpoint}")
    private String ingestionEndpoint;

    public ParallelDataSimulator(RestTemplate restTemplate,
                                 @Value("${simulation.parallel-threads}") int parallelThreads) {
        this.restTemplate = restTemplate;
        this.executorService = Executors.newFixedThreadPool(parallelThreads);
    }

    @Override
    public void run(String... args) {
        log.info("ParallelDataSimulator started...");
    }

    @Scheduled(fixedRateString = "${simulation.interval-ms}")
    public void sendMockData() {
        int batchSize = requestsPerInterval / parallelThreads;
        int remainder = requestsPerInterval % parallelThreads;
        CountDownLatch batchDone = new CountDownLatch(parallelThreads);

        for (int i = 0; i < parallelThreads; i++) {
            int requestsForThread = batchSize + (i < remainder ? 1 : 0);
            executorService.submit(() -> {
                try {
                    ThreadLocalRandom random = ThreadLocalRandom.current();
                    for (int j = 0; j < requestsForThread; j++) {
                        EnergyUsageDto dto = EnergyUsageDto.builder()
                                .deviceId(random.nextLong(1, 201))
                                .energyConsumed(Math.round(random.nextDouble(0.0, 2.0) * 100.0) / 100.0)
                                .timestamp(LocalDateTime.now()
                                        .atZone(ZoneId.systemDefault()).toInstant())
                                .build();
                        try {
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            HttpEntity<EnergyUsageDto> request = new HttpEntity<>(dto, headers);
                            restTemplate.postForEntity(ingestionEndpoint, request, Void.class);
                            log.debug("Sent mock data: {}", dto);
                        } catch (Exception e) {
                            log.error("Failed to send data: {}", e.getMessage());
                        }
                    }
                } finally {
                    batchDone.countDown();
                }
            });
        }

        // Wait for this batch to finish before returning: the (single-threaded)
        // scheduler only triggers the next batch afterwards -> natural backpressure.
        try {
            batchDone.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        log.info("ParallelDataSimulator shut down.");
    }
}
