package com.infinite.busTicket.controller;

import com.infinite.busTicket.entity.dto.BusDTO;
import com.infinite.busTicket.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/bus")
@CrossOrigin
public class BusController {

    @Autowired
    private BusService busService;

    @GetMapping
    public ResponseEntity<?> getAllBus()
    {
        return new ResponseEntity<>(busService.getAllBus(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createBus(@RequestBody BusDTO bus)
    {
        if(bus.getTimeOfDropping().isBefore(bus.getTimeOfBoarding()))
        {
            return new ResponseEntity<>("Drop before Board",HttpStatus.CREATED);
        }
        busService.createBus(bus);
        return new ResponseEntity<>("Created",HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBus(@PathVariable Long id,@RequestBody BusDTO bus)
    {
        busService.updateBus(id,bus);
        return new ResponseEntity<>("Updated",HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id)
    {
        return new ResponseEntity<>(busService.getById(id),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBus(@PathVariable Long id)
    {
        busService.deleteBus(id);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }

    @GetMapping("/{source}/{destination}/{doj}")
    public ResponseEntity<?> searchBus(@PathVariable String source,
                                          @PathVariable String destination,
                                          @PathVariable LocalDate doj)
    {

        return new ResponseEntity<>(busService.searchBus(source,destination,doj),HttpStatus.OK);
    }

    @GetMapping("/location")
    public ResponseEntity<?> getLocation()
    {
        return new ResponseEntity<>(busService.getAllLocation(),HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getBusByUserId(@PathVariable Long id)
    {
        return new ResponseEntity<>(busService.getBusByUserId(id),HttpStatus.OK);
    }

    @GetMapping("/{id}/tickets")
    public ResponseEntity<?> getTicketsFromBus(@PathVariable Long id)
    {
        return new ResponseEntity<>(busService.getTicketsFromBus(id),HttpStatus.OK);
    }

    @GetMapping("/{id}/seats")
    public ResponseEntity<?> getSeatsFromBusId(@PathVariable Long id)
    {
        return new ResponseEntity<>(busService.getSeatsFromBusId(id),HttpStatus.OK);
    }
}
