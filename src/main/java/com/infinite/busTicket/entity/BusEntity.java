package com.infinite.busTicket.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String busNumber;

    private String source;
    private String destination;
    private LocalTime timeOfBoarding;
    private LocalTime timeOfDropping;
    private LocalDate dateOfJourney;

//    @JdbcTypeCode(SqlTypes.JSON)
//    @JsonDeserialize(using = CustomDateMapDeserializer.class)
//    private Map<String,Date> boardingPlacesandTime;
    private List<String> boardingPlaces;
    private List<String> dropOffPlaces;
    private int seats;

    @ManyToOne
    @JoinColumn(name="conductor_id")
    private Users conductor;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TicketEntity> tickets;
}
