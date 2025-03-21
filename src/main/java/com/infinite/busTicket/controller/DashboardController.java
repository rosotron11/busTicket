package com.infinite.busTicket.controller;

import com.infinite.busTicket.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/total-stats")
    public ResponseEntity<?> getStats()
    {
        return new ResponseEntity<>(dashboardService.getStats(), HttpStatus.OK);
    }
    @GetMapping("/daily-ticket-stats/{date}")
    public ResponseEntity<?> getDailyTicketStats(@PathVariable String date)
    {
        return new ResponseEntity<>(dashboardService.getDailyTicketStats(date),HttpStatus.OK);
    }

    @GetMapping("/daily-bus-stats/{date}")
    public ResponseEntity<?> getDailyBusStats(@PathVariable String date)
    {
        return new ResponseEntity<>(dashboardService.getDailyBusStats(date),HttpStatus.OK);
    }

    @GetMapping("/{id}/total-stats")
    public ResponseEntity<?> getStatsByConductorId(@PathVariable Long id)
    {
        return new ResponseEntity<>(dashboardService.getStatsByConductorId(id), HttpStatus.OK);
    }
    @GetMapping("/{id}/daily-ticket-stats/{date}")
    public ResponseEntity<?> getDailyTicketStatsByConductorId(@PathVariable Long id,
                                                                  @PathVariable String date)
    {
        return new ResponseEntity<>(dashboardService.getDailyTicketStatsByConductorId(id,date),HttpStatus.OK);
    }

    @GetMapping("/{id}/daily-bus-stats/{date}")
    public ResponseEntity<?> getDailyBusStatsByConductorId(@PathVariable Long id,
                                                           @PathVariable String date)
    {
        return new ResponseEntity<>(dashboardService.getDailyBusStatsByConductorId(id,date),HttpStatus.OK);
    }
}
