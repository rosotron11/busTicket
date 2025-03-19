package com.infinite.busTicket.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {
    private Long id;
    private String ticketNumber;
    private String paymentStatus;
    private String source;
    private String destination;
    private LocalDate dateOfJourney;
    private LocalTime boardingTime;
    private List<Map<String,String>> passengers;
    private String email;
    private String amount;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserDTO bookingUser;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BusDTO bus;
}
