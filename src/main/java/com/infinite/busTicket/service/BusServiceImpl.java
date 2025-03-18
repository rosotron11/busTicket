package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.BusEntity;
import com.infinite.busTicket.entity.Districts;
import com.infinite.busTicket.entity.TicketEntity;
import com.infinite.busTicket.entity.Users;
import com.infinite.busTicket.entity.response.BusListResponse;
import com.infinite.busTicket.entity.response.SearchResponse;
import com.infinite.busTicket.entity.response.TicketResponse;
import com.infinite.busTicket.repository.BusRepository;
import com.infinite.busTicket.repository.TicketRepository;
import com.infinite.busTicket.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<BusListResponse> getAllBus() {
        List<BusListResponse> busListResponses=new ArrayList<>();
        busRepository.findAll().forEach(x->{
            busListResponses.add(modelMapper.map(x, BusListResponse.class));
        });
        return busListResponses;
    }

    @Override
    public void createBus(BusListResponse bus) {
        busRepository.save(modelMapper.map(bus,BusEntity.class));
    }

    @Override
    public void updateBus(Long id, BusListResponse bus) {
        busRepository.save(modelMapper.map(bus,BusEntity.class));
    }

    @Override
    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }

    @Override
    public BusListResponse getById(Long id)
    {
        return modelMapper.map(busRepository.findById(id).orElse(new BusEntity()),
                BusListResponse.class);
    }

    @Override
    public List<BusListResponse> searchBus(String source, String destination, LocalDate doj) {
        List<BusEntity> buses=busRepository.findBySourceAndDestinationAndDateOfJourney(
                source,destination,doj
        );
        List<BusListResponse> busListResponses=new ArrayList<>();
        buses.forEach(x->{
            busListResponses.add(modelMapper.map(x,BusListResponse.class));
        });
        return busListResponses;
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

    @Override
    public List<BusListResponse> getBusByUserId(Long id) {
        Users user=userRepository.findById(id).orElse(new Users());
        List<BusListResponse> busListResponses=new ArrayList<>();
        user.getBuses().forEach(x->
        {
            busListResponses.add(modelMapper.map(x, BusListResponse.class));
        });
        return busListResponses;
    }

    @Override
    public List<TicketResponse> getTicketsFromBus(Long id) {
        BusEntity bus=busRepository.findById(id).orElse(new BusEntity());
        List<TicketResponse> tickets= new ArrayList<>();
        bus.getTickets().forEach(x->{
            tickets.add(modelMapper.map(x,TicketResponse.class));
        });
        return tickets;
    }
}
