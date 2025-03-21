package com.infinite.busTicket.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name="Bus")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String busNumber;
    private String vendorName;

    private LocalDateTime createdOn;

    //@Enumerated(EnumType.STRING)
    private String source;

    //@Enumerated(EnumType.STRING)
    private String destination;

    private LocalTime timeOfBoarding;
    private LocalTime timeOfDropping;
    private LocalDate dateOfJourney;
    private int bookedSeats;
    private int totalSeats;

    @ElementCollection
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Map<String,String>> boardingPlaces;

    @ElementCollection
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Map<String,String>> dropOffPlaces;

    private float price;

    @ManyToOne
    @JoinColumn(name="conductor_id")
    private Users conductor;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TicketEntity> tickets;
}
