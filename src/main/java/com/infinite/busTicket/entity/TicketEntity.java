package com.infinite.busTicket.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    private String seatNo;

    @ManyToOne
    @JoinColumn(name="passenger_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Users passenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bus_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BusEntity bus;
}
