package com.infinite.busTicket.entity.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infinite.busTicket.entity.TicketEntity;
import com.infinite.busTicket.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusListResponse {
    private Long id;
    private String busNumber;
    private String source;
    private String destination;
    private LocalTime timeOfBoarding;
    private LocalTime timeOfDropping;
    private LocalDate dateOfJourney;
    private List<String> boardingPlaces;
    private List<String> dropOffPlaces;
    private int seats;
    private UserResponse conductor;

    @JsonIgnore
    private List<TicketResponse> tickets;
}
