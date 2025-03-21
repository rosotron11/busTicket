package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.BusEntity;
import com.infinite.busTicket.entity.TicketEntity;
import com.infinite.busTicket.entity.Users;
import com.infinite.busTicket.entity.dto.BusDTO;
import com.infinite.busTicket.entity.dto.TicketDTO;
import com.infinite.busTicket.repository.BusRepository;
import com.infinite.busTicket.repository.TicketRepository;
import com.infinite.busTicket.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService{

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<TicketDTO> getAllTickets() {
        List<TicketDTO> ticketLists=new ArrayList<>();
        return ticketLists;
    }

    @Override
    public void createTicket(TicketDTO ticket) {
        BusEntity bus=busRepository.findById(ticket.getBus().getId()).orElse(new BusEntity());
        bus.setBookedSeats(bus.getBookedSeats()+ticket.getPassengers().size());
        busRepository.save(bus);
        ticket.setBus(modelMapper.map(bus, BusDTO.class));
        ticket.setBookingTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        ticketRepository.save(modelMapper.map(ticket,TicketEntity.class));
    }

    @Override
    public void deleteTicket(Long id) {
        TicketEntity ticket=ticketRepository.findById(id).orElse(new TicketEntity());
        BusEntity bus=busRepository.findById(ticket.getBus().getId()).orElse(new BusEntity());
        bus.setBookedSeats(bus.getBookedSeats()-ticket.getPassengers().size());
        busRepository.save(bus);
        ticketRepository.deleteById(id);
    }

    @Override
    public List<TicketDTO> getTicketsByUserId(Long id) {
        Users user=userRepository.findById(id).orElse(new Users());
        List<TicketDTO> tickets= new ArrayList<>();
        user.getTickets().forEach(x->
                {
                    tickets.add(modelMapper.map(x, TicketDTO.class));
                });
        return tickets;
    }

    @Override
    public void completePayment(String id) {
        TicketEntity ticket=ticketRepository.findById(Long.valueOf(id)).orElse(new TicketEntity());
        ticket.setPaymentStatus("Confirmed");
        ticket.setPaymentTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        ticketRepository.save(ticket);
    }
}
