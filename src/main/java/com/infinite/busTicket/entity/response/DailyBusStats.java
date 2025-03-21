package com.infinite.busTicket.entity.response;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyBusStats {
    private Long totalBus;
    private Long totalPassengers;
    private Long totalVendor;
    private Float avPassengerPerBus;
    private Duration totalTime;
    private Duration avJourneyTime;
    private Float totalAmount;
    private Float averageAmount;
}
