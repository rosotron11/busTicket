package com.infinite.busTicket.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class BusDTO {
    private Long id;
    private String busNumber;
    private String source;
    private String destination;
    private LocalTime timeOfBoarding;
    private LocalTime timeOfDropping;
    private LocalDate dateOfJourney;
    private List<Map<String,String>> boardingPlaces;
    private List<Map<String,String>> dropOffPlaces;
    private int seats;
    private UserDTO conductor;
    private float price;

    @JsonIgnore
    private List<TicketDTO> tickets;
}
