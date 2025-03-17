package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.BusEntity;
import com.infinite.busTicket.entity.response.SearchResponse;
import com.infinite.busTicket.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository busRepository;

    @Override
    public List<BusEntity> getAllBus() {
        return busRepository.findAll();
    }

    @Override
    public void createBus(BusEntity bus) {
        busRepository.save(bus);
    }

    @Override
    public void updateBus(Long id, BusEntity bus) {
        busRepository.save(bus);
    }

    @Override
    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }

    @Override
    public BusEntity getById(Long id)
    {
        return busRepository.findById(id).orElse(new BusEntity());
    }

    @Override
    public List<BusEntity> searchBus(String source, String destination, LocalDate doj) {
        List<BusEntity> busBySource=busRepository.findBySource(source);
        List<BusEntity> busByDestination=busRepository.findByDestination(destination);
        List<BusEntity> busByDOJ=busRepository.findByDateOfJourney(doj);
        busBySource.retainAll(busByDestination);
        busBySource.retainAll(busByDOJ);
        return busBySource;
    }

    @Override
    public List<SearchResponse> getAllLocation() {
        List<BusEntity> buses=busRepository.findAll();
        Set<String> locations=new HashSet<>();
        List<SearchResponse> searchResponses= new ArrayList<>();
        for(BusEntity bus:buses)
        {
            if(locations.add(bus.getSource()))
            {
                SearchResponse searchResponse=new SearchResponse();
                searchResponse.setLocation(bus.getSource());
                searchResponses.add(searchResponse);
            }
            if(locations.add(bus.getDestination()))
            {
                SearchResponse searchResponse=new SearchResponse();
                searchResponse.setLocation(bus.getDestination());
                searchResponses.add(searchResponse);
            }
        }
        return searchResponses;
    }
}
