package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.TicketEntity;
import com.infinite.busTicket.entity.response.TicketResponse;

import java.util.List;

public interface TicketService {
    List<TicketResponse> getAllTickets();
    void createTicket(TicketResponse ticket);
    void deleteTicket(Long id);

    List<TicketResponse> getTicketsByUserId(Long id);
}
