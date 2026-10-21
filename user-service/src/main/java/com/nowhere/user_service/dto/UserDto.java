package com.nowhere.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record UserDto (
  Long id,
  @NotBlank(message = "First name is required")
  String firstName,
  @NotBlank(message = "Last name is required")
  String lastName,
  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  String email,
  @NotBlank(message = "Address is required")
  String address,
  // optional fields: omitted/null in the request body means false / 0.0
  Boolean alerting,
  @PositiveOrZero(message = "Threshold must be positive or zero")
  Double energyAlertingThreshold
  ){}
