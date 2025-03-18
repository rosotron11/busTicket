package com.infinite.busTicket.entity.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infinite.busTicket.entity.BusEntity;
import com.infinite.busTicket.entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {
    private Long id;
    private String ticketNumber;
    private String paymentStatus;
    private String source;
    private String destination;
    private LocalDate dateOfJourney;
    private LocalTime boardingTime;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserResponse passenger;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BusListResponse bus;
}
