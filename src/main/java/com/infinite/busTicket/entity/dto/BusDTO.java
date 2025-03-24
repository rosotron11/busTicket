package com.infinite.busTicket.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusDTO {
    private Long id;
    private LocalDateTime createdOn;

    @NonNull
    private String busNumber;

    @NonNull
    private String vendorName;

    @NonNull
    private String source;

    @NonNull
    private String destination;

    @NonNull
    private LocalTime timeOfBoarding;

    @NonNull
    private LocalTime timeOfDropping;

    @NonNull
    private LocalDate dateOfJourney;

    @NonNull
    private List<Map<String,String>> boardingPlaces;

    @NonNull
    private List<Map<String,String>> dropOffPlaces;

    @NonNull
    private int bookedSeats;

    @NonNull
    private int totalSeats;

    @NonNull
    private UserDTO conductor;

    @NonNull
    private float price;

    @JsonIgnore
    private List<TicketDTO> tickets;
}
