package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.dto.BusDTO;
import com.infinite.busTicket.entity.response.SearchResponse;
import com.infinite.busTicket.entity.dto.TicketDTO;

import java.time.LocalDate;
import java.util.List;

public interface BusService {
    List<BusDTO> getAllBus();
    void createBus(BusDTO bus);
    void updateBus(Long id, BusDTO bus);
    BusDTO getById(Long id);
    void deleteBus(Long id);
    List<BusDTO> searchBus(String source, String destination, LocalDate doj);

    List<SearchResponse> getAllLocation();

    List<BusDTO> getBusByUserId(Long id);

    List<TicketDTO> getTicketsFromBus(Long id);

    List<Integer> getSeatsFromBusId(Long id);
}
