package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.BusEntity;
import com.infinite.busTicket.entity.TicketEntity;
import com.infinite.busTicket.entity.Users;
import com.infinite.busTicket.entity.dto.BusDTO;
import com.infinite.busTicket.entity.response.SearchResponse;
import com.infinite.busTicket.entity.dto.TicketDTO;
import com.infinite.busTicket.repository.BusRepository;
import com.infinite.busTicket.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<BusDTO> getAllBus() {
        List<BusDTO> busListRespons =new ArrayList<>();
        busRepository.findAll().forEach(x->{
            busListRespons.add(modelMapper.map(x, BusDTO.class));
        });
        return busListRespons;
    }

    @Override
    public void createBus(BusDTO bus) {
        busRepository.save(modelMapper.map(bus,BusEntity.class));
    }

    @Override
    public void updateBus(Long id, BusDTO bus) {
        busRepository.save(modelMapper.map(bus,BusEntity.class));
    }

    @Override
    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }

    @Override
    public BusDTO getById(Long id)
    {
        return modelMapper.map(busRepository.findById(id).orElse(new BusEntity()),
                BusDTO.class);
    }

    @Override
    public List<BusDTO> searchBus(String source, String destination, LocalDate doj) {
        List<BusEntity> buses=busRepository.findBySourceAndDestinationAndDateOfJourney(
                source,destination,doj
        );
        List<BusDTO> busListRespons =new ArrayList<>();
        buses.forEach(x->{
            busListRespons.add(modelMapper.map(x, BusDTO.class));
        });
        return busListRespons;
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
    public List<BusDTO> getBusByUserId(Long id) {
        Users user=userRepository.findById(id).orElse(new Users());
        List<BusDTO> busListRespons =new ArrayList<>();
        user.getBuses().forEach(x->
        {
            busListRespons.add(modelMapper.map(x, BusDTO.class));
        });
        return busListRespons;
    }

    @Override
    public List<TicketDTO> getTicketsFromBus(Long id) {
        BusEntity bus=busRepository.findById(id).orElse(new BusEntity());
        List<TicketDTO> tickets= new ArrayList<>();
        bus.getTickets().forEach(x->{
            tickets.add(modelMapper.map(x, TicketDTO.class));
        });
        return tickets;
    }

    @Override
    public List<Integer> getSeatsFromBusId(Long id) {
        final int[] count = {1};
        BusEntity bus = busRepository.findById(id).orElse(new BusEntity());
        List<TicketEntity> tickets = bus.getTickets();
        List<Integer> seats = new ArrayList<>();

        tickets.forEach(x -> {
            x.getPassengers().forEach(y -> {
                Collection<String> seatsNo = y.values();
                seatsNo.forEach(v -> {
                    if (count[0] % 2 == 0) {
                        seats.add(Integer.valueOf(v));
                    }
                    count[0]++;
                });
            });
        });

        return seats;
    }
}
