package com.nowhere.device_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
public record ErrorDto (
     int status,
     String message,
     LocalDateTime timestamp
){}