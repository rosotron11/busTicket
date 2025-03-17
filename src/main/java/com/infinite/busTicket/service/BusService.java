package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.BusEntity;
import com.infinite.busTicket.entity.response.SearchResponse;

import java.time.LocalDate;
import java.util.List;

public interface BusService {
    List<BusEntity> getAllBus();
    void createBus(BusEntity bus);
    void updateBus(Long id, BusEntity bus);
    BusEntity getById(Long id);
    void deleteBus(Long id);
    List<BusEntity> searchBus(String source, String destination, LocalDate doj);

    List<SearchResponse> getAllLocation();
}
