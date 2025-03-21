package com.infinite.busTicket.controller;

import com.infinite.busTicket.entity.dto.TicketDTO;
import com.infinite.busTicket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getTicketByUserId(@PathVariable Long id){
        return new ResponseEntity<>(ticketService.getTicketsByUserId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody TicketDTO ticket)
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

    @PutMapping("/pay")
    public  ResponseEntity<?> completePayment(@RequestBody String id)
    {
        ticketService.completePayment(id);
        return new ResponseEntity<>("Payment Completed",HttpStatus.OK);
    }
}
