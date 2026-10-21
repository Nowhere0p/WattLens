package com.nowhere.user_service.dto;

import java.time.LocalDateTime;


public record ErrorDto(
        int status,
        String message,
        LocalDateTime timestamp
){}