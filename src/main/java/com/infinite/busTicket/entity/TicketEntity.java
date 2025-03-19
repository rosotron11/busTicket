package com.infinite.busTicket.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @ElementCollection
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Map<String,String>> passengers;

    private String email;
    private float amount;

    private String paymentStatus;
    private String source;
    private String destination;
    private LocalDate dateOfJourney;
    private LocalTime boardingTime;

    @ManyToOne
    @JoinColumn(name="passenger_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Users bookingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bus_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BusEntity bus;
}
