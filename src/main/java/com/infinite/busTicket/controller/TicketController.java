package com.infinite.busTicket.controller;

import com.infinite.busTicket.entity.TicketEntity;
import com.infinite.busTicket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/tickets")
@CrossOrigin
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public ResponseEntity<?> getTicket()
    {
        return new ResponseEntity<>(ticketService.getAllTickets(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody TicketEntity ticket)
    {
        ticketService.createTicket(ticket);
        return new ResponseEntity<>("Created",HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id)
    {
        ticketService.deleteTicket(id);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }
}
