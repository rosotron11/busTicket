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

    //@Enumerated(EnumType.STRING)
    private String source;

    //@Enumerated(EnumType.STRING)
    private String destination;

    private LocalTime timeOfBoarding;
    private LocalTime timeOfDropping;
    private LocalDate dateOfJourney;

//    @JdbcTypeCode(SqlTypes.JSON)
//    @JsonDeserialize(using = CustomDateMapDeserializer.class)
//    private Map<String,Date> boardingPlacesandTime;
    private List<String> boardingPlaces;
    private List<String> dropOffPlaces;

    @ManyToOne
    @JoinColumn(name="conductor_id")
    private Users conductor;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TicketEntity> tickets;
}
