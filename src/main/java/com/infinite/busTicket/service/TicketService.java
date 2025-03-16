package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.TicketEntity;

import java.util.List;

public interface TicketService {
    List<TicketEntity> getAllTickets();
    void createTicket(TicketEntity ticket);
    void deleteTicket(Long id);
}
