package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.dto.TicketDTO;

import java.util.List;

public interface TicketService {
    List<TicketDTO> getAllTickets();
    void createTicket(TicketDTO ticket);
    void deleteTicket(Long id);

    List<TicketDTO> getTicketsByUserId(Long id);

    void completePayment(String id);
}
