package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.BusEntity;
import com.infinite.busTicket.entity.TicketEntity;
import com.infinite.busTicket.repository.BusRepository;
import com.infinite.busTicket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService{

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BusRepository busRepository;

    @Override
    public List<TicketEntity> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public void createTicket(TicketEntity ticket) {
//        BusEntity bus=ticket.getBus();
//        System.out.println(bus.toString());
//        System.out.println(bus.getSeats());
//        bus.setSeats(bus.getSeats()-1);
//        busRepository.save(bus);
//        ticket.setBus(bus);
        ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
