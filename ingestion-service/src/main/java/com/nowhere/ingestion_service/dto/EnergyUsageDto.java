package com.nowhere.ingestion_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.Instant;

@Builder
public record EnergyUsageDto (
        @NotNull(message = "Device ID is required")
        Long deviceId,
        @Positive(message = "Energy consumed must be greater than zero")
        double energyConsumed,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @NotNull(message = "Timestamp is required")
        Instant timestamp) {}
