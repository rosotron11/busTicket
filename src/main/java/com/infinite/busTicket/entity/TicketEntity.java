package com.infinite.busTicket.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "Ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private String ticketNumber;

    @PostLoad
    @PostPersist
    public void generateTicketNumber() {
        this.ticketNumber = String.format("T%03d", this.id);
    }

    private String paymentStatus;
    private String source;
    private String destination;
    private LocalDate dateOfJourney;
    private LocalTime boardingTime;

    @ManyToOne
    @JoinColumn(name="passenger_id")
    private Users passenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bus_id")
    private BusEntity bus;
}
