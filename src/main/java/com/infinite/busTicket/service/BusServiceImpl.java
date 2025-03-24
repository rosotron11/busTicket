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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
    public List<BusDTO> getAllBus() {
        List<BusDTO> busListRespons =new ArrayList<>();
        busRepository.findAll().forEach(x->{
            busListRespons.add(modelMapper.map(x, BusDTO.class));
        });
        return busListRespons;
    }

    @Override
    public String createBus(BusDTO bus) {
        List<BusEntity> busEntity=busRepository.findByBusNumber(bus.getBusNumber());
        boolean unique=true;
        for (BusEntity x : busEntity) {
            if (x.getDateOfJourney().equals(bus.getDateOfJourney())
                    && x.getTimeOfDropping().isAfter(bus.getTimeOfBoarding())) {
                unique = false;
                break;
            }
        }
        if(!unique)
        {
            return "Bus is still on route during time of boarding";
        }

        for (Map<String, String> place : bus.getBoardingPlaces()) {
            int count=1;
            for (Map.Entry<String, String> entry : place.entrySet()) {
                if (count % 2 == 0)
                {
                    LocalTime boardingTime = LocalTime.parse(entry.getValue());
                    if (boardingTime.isBefore(bus.getTimeOfBoarding()) || boardingTime.isAfter(bus.getTimeOfDropping())
                            || Duration.between(bus.getTimeOfBoarding(), boardingTime).toHours() > 2) {
                        return "Invalid boarding time in boarding places";
                    }
                }
                count++;
            }
        }

        for (Map<String, String> place : bus.getDropOffPlaces()) {
            int count=1;
            for (Map.Entry<String, String> entry : place.entrySet()) {
                if(count%2==0){
                    LocalTime droppingTime = LocalTime.parse(entry.getValue());
                    if (droppingTime.isAfter(bus.getTimeOfDropping()) || droppingTime.isBefore(bus.getTimeOfBoarding())
                            || Duration.between(droppingTime, bus.getTimeOfDropping()).toHours() > 2) {
                        return "Invalid dropping time in drop off places";
                    }
                }
                count++;
            }
        }

        bus.setCreatedOn(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        bus.setBookedSeats(0);
        busRepository.save(modelMapper.map(bus,BusEntity.class));
        return "Created";
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
