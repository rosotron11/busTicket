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
        bus.setSeats(bus.getSeats()-1);
        busRepository.save(bus);
        ticket.setBus(modelMapper.map(bus, BusDTO.class));
        ticketRepository.save(modelMapper.map(ticket,TicketEntity.class));
    }

    @Override
    public void deleteTicket(Long id) {
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
}
