package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.BusEntity;
import com.infinite.busTicket.entity.TicketEntity;
import com.infinite.busTicket.entity.Users;
import com.infinite.busTicket.entity.response.BusListResponse;
import com.infinite.busTicket.entity.response.SearchResponse;
import com.infinite.busTicket.entity.response.TicketResponse;

import java.time.LocalDate;
import java.util.List;

public interface BusService {
    List<BusListResponse> getAllBus();
    void createBus(BusListResponse bus);
    void updateBus(Long id, BusListResponse bus);
    BusListResponse getById(Long id);
    void deleteBus(Long id);
    List<BusListResponse> searchBus(String source, String destination, LocalDate doj);

    List<SearchResponse> getAllLocation();

    List<BusListResponse> getBusByUserId(Long id);

    List<TicketResponse> getTicketsFromBus(Long id);
}
