package com.actech.uber.model;

import lombok.Getter;

@Getter
public enum BookingStatus {
    CANCELLED,
    SCHEDULED,
    ASSIGNING_DRIVER,
    CAB_ARRIVED,
    IN_RIDE,
    COMPLETED
}
