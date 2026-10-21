package com.nowhere.device_service.dto;

import com.nowhere.device_service.model.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record DeviceDto (
        Long id,
        @NotBlank(message = "Device name is required")
        String name,
        @NotBlank(message = "Location is required")
        String location,
        @NotNull(message = "Device type is required")
        DeviceType type,
        @NotNull(message = "User ID is required")
        Long userId
        ){}
