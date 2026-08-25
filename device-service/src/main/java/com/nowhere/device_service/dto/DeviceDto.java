package com.nowhere.device_service.dto;

import com.nowhere.device_service.model.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDto {
    private Long id;
    private  String name;
    private String location;
    private DeviceType type;
    private Long userId;
}
