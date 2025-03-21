package com.infinite.busTicket.service;

import com.infinite.busTicket.entity.BusEntity;
import com.infinite.busTicket.entity.TicketEntity;
import com.infinite.busTicket.entity.Users;
import com.infinite.busTicket.entity.response.AllTimeStats;
import com.infinite.busTicket.entity.response.DailyBusStats;
import com.infinite.busTicket.entity.response.DailyTicketStats;
import com.infinite.busTicket.repository.BusRepository;
import com.infinite.busTicket.repository.TicketRepository;
import com.infinite.busTicket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.datatransfer.FlavorEvent;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DashboardServiceImpl implements DashboardService{
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DailyBusStats getDailyBusStats(String date) {
        LocalDate selectedDate=LocalDate.parse(date);
        DailyBusStats dailyBusStats=new DailyBusStats();
        final List<BusEntity>[] buses = new List[]{busRepository.findAll()};
        final Long[] totalBus = {Long.valueOf(0)};
        final Long[] totalPassenger = {Long.valueOf(0)};
        final Duration[] totalTime = {Duration.ZERO};
        final Float[] totalAmount = {Float.valueOf(0)};
        Float avPassengerPerBus=0f;
        Duration avJourneyTime=Duration.ZERO;
        Float averageAmount=0f;
        Set<String> vendors=new HashSet<>();
        buses[0].forEach(x->{
            if(x.getDateOfJourney().isEqual(selectedDate))
            {
                totalBus[0] +=1;
                vendors.add(x.getVendorName());
                x.getTickets().forEach(y->{
                    totalPassenger[0] +=y.getPassengers().size();
                    totalAmount[0] +=y.getAmount();
                });
                totalTime[0] = totalTime[0].plus(Duration.between(
                        x.getTimeOfDropping(),x.getTimeOfBoarding()
                ));
            }
        });
        if(totalBus[0]>0)
        {
            avPassengerPerBus=((float)totalPassenger[0])/((float)totalBus[0]);
            avJourneyTime=totalTime[0].dividedBy(totalBus[0]);
            averageAmount=totalAmount[0]/((float)totalBus[0]);
        }
        dailyBusStats.setAverageAmount(averageAmount);
        dailyBusStats.setAvJourneyTime(avJourneyTime);
        dailyBusStats.setAvPassengerPerBus(avPassengerPerBus);
        dailyBusStats.setTotalBus(totalBus[0]);
        dailyBusStats.setTotalVendor((long) vendors.size());
        dailyBusStats.setTotalPassengers(totalPassenger[0]);
        dailyBusStats.setTotalTime(totalTime[0]);
        dailyBusStats.setTotalAmount(totalAmount[0]);
        return dailyBusStats;
    }

    @Override
    public DailyTicketStats getDailyTicketStats(String date) {
        DailyTicketStats dailyTicketStats=new DailyTicketStats();
        final Float[] amount = {Float.valueOf(0)};
        LocalDate localDate=LocalDate.parse(date);
        List<TicketEntity> tickets=ticketRepository.findAll();
        final Long[] bookedTickets = {Long.valueOf(0)};
        final Long[] totalDailyTickets = {Long.valueOf(0)};
        final Long[] paidTickets = {Long.valueOf(0)};
        tickets.forEach(x->{
            if(x.getBookingTime().toLocalDate().isEqual(localDate)) {
                amount[0] +=x.getAmount();
                if (x.getPaymentTime() == null && x.getBookingTime() != null) {
                    bookedTickets[0] += 1;
                } else {
                    paidTickets[0] += 1;
                }
                totalDailyTickets[0] += 1;
            }
        });
        dailyTicketStats.setTotalTicket(totalDailyTickets[0]);
        dailyTicketStats.setBookedTicket(bookedTickets[0]);
        dailyTicketStats.setPaidTicket(paidTickets[0]);
        dailyTicketStats.setTotalAmount(amount[0]);
        dailyTicketStats.setAverageAmount(amount[0]/paidTickets[0]);
        return dailyTicketStats;
    }

    @Override
    public AllTimeStats getStats() {
        AllTimeStats allTimeStats=new AllTimeStats();
        List<TicketEntity> tickets=ticketRepository.findAll();
        final Long[] bookedTickets = {Long.valueOf(0)};
        final Long[] paidTickets = {Long.valueOf(0)};
        final Long[] passenger = {Long.valueOf(0)};
        Set<String> vendors=new HashSet<>();
        tickets.forEach(x->{
            if(x.getPaymentTime()==null && x.getBookingTime()!=null)
            {
                bookedTickets[0] +=1;
            }
            else {
                paidTickets[0] +=1;
            }
            passenger[0] +=x.getPassengers().size();
        });
        List<BusEntity> buses=busRepository.findAll();
        buses.forEach(x->
                vendors.add(x.getVendorName()));
        final Float[] totalAmount = {Float.valueOf(0)};
        tickets.forEach(x->{
            totalAmount[0] +=x.getAmount();
        });
        allTimeStats.setTotalBus((long) buses.size());
        allTimeStats.setTotalAmount(totalAmount[0]);
        allTimeStats.setPaidTicket(paidTickets[0]);
        allTimeStats.setTotalTicket((long) tickets.size());
        allTimeStats.setBookedTicket(bookedTickets[0]);
        allTimeStats.setTotalPassenger(passenger[0]);
        allTimeStats.setAverageAmountPerBus(totalAmount[0]/buses.size());
        allTimeStats.setAverageAmountPerTicket(totalAmount[0]/paidTickets[0]);
        allTimeStats.setTotalVendor((long) vendors.size());
        return allTimeStats;
    }

    @Override
    public AllTimeStats getStatsByConductorId(Long id) {
        AllTimeStats allTimeStats=new AllTimeStats();
        Users user=userRepository.findById(id).orElse(new Users());
        List<BusEntity> buses=user.getBuses();
        Set<String> vendors=new HashSet<>();
        Long totalBus=(long) buses.size();
        Long totalVendor;
        Long totalPassenger = 0L;
        Long totalTicket = 0L;
        Long bookedTicket=0l;
        Long paidTicket=0l;
        Float totalAmount=0f;
        Float averageAmountPerTicket;
        Float averageAmountPerBus;
        for(BusEntity bus:buses)
        {
            List<TicketEntity> tickets=bus.getTickets();
            totalTicket+=tickets.size();
            vendors.add(bus.getVendorName());
            for(TicketEntity ticket:tickets)
            {
                if(ticket.getPaymentTime()==null && ticket.getBookingTime()!=null)
                {
                    bookedTicket+=1;
                }
                else
                {
                    paidTicket+=1;
                }
                totalPassenger+=ticket.getPassengers().size();
                totalAmount+=ticket.getAmount();
            }
        }
        totalVendor= (long) vendors.size();
        averageAmountPerBus=totalAmount/totalBus;
        averageAmountPerTicket=totalAmount/totalTicket;
        allTimeStats.setTotalTicket(totalTicket);
        allTimeStats.setTotalPassenger(totalPassenger);
        allTimeStats.setTotalBus(totalBus);
        allTimeStats.setTotalAmount(totalAmount);
        allTimeStats.setPaidTicket(paidTicket);
        allTimeStats.setBookedTicket(bookedTicket);
        allTimeStats.setTotalVendor(totalVendor);
        allTimeStats.setAverageAmountPerTicket(averageAmountPerTicket);
        allTimeStats.setAverageAmountPerBus(averageAmountPerBus);
        return allTimeStats;
    }

    @Override
    public DailyTicketStats getDailyTicketStatsByConductorId(Long id, String date) {
        LocalDate searchDate=LocalDate.parse(date);
        DailyTicketStats dailyTicketStats=new DailyTicketStats();
        Users user=userRepository.findById(id).orElse(new Users());
        List<BusEntity> buses=user.getBuses();
        Long totalTicket = 0L;
        Long bookedTicket=0l;
        Long paidTicket=0l;
        Float totalAmount=0f;
        Float averageAmount;
        for(BusEntity bus:buses)
        {
            List<TicketEntity> tickets=bus.getTickets();
            for(TicketEntity ticket:tickets)
            {
                if(ticket.getBookingTime().toLocalDate().isEqual(searchDate))
                {
                    if(ticket.getPaymentTime()==null && ticket.getBookingTime()!=null)
                    {
                        bookedTicket+=1;
                    }
                    else
                    {
                        paidTicket+=1;
                    }
                    totalTicket+=1;
                    totalAmount+=ticket.getAmount();
                }
            }
        }
        averageAmount=totalAmount/totalTicket;
        dailyTicketStats.setTotalTicket(totalTicket);
        dailyTicketStats.setBookedTicket(bookedTicket);
        dailyTicketStats.setPaidTicket(paidTicket);
        dailyTicketStats.setTotalAmount(totalAmount);
        dailyTicketStats.setAverageAmount(averageAmount);
        return dailyTicketStats;
    }

    @Override
    public DailyBusStats getDailyBusStatsByConductorId(Long id, String date) {
        DailyBusStats dailyBusStats=new DailyBusStats();
        LocalDate searchDate=LocalDate.parse(date);
        Users user=userRepository.findById(id).orElse(new Users());
        List<BusEntity> buses=user.getBuses();
        Set<String> vendors=new HashSet<>();
        Long totalVendor=0l;
        Long totalBus= 0l;
        Long totalPassengers = 0L;
        Float avPassengerPerBus=0f;
        Duration totalTime=Duration.ZERO;
        Duration avJourneyTime=Duration.ZERO;
        Float totalAmount = 0f;
        Float averageAmount=0f;
        for(BusEntity bus:buses)
        {
            if(bus.getDateOfJourney().isEqual(searchDate))
            {
                List<TicketEntity> tickets=bus.getTickets();
                vendors.add(bus.getVendorName());
                for(TicketEntity ticket:tickets)
                {
                    totalPassengers+=ticket.getPassengers().size();
                    totalAmount+=ticket.getAmount();
                }
                totalTime=totalTime.plus(Duration.between(bus.getTimeOfDropping(),bus.getTimeOfBoarding()));
                totalBus+=1;
            }
        }
        if(totalBus>0)
        {
            averageAmount=totalAmount/totalBus;
            avPassengerPerBus= (float) (totalPassengers/totalBus);
            avJourneyTime=totalTime.dividedBy(totalBus);
        }
        totalVendor= (long) vendors.size();
        dailyBusStats.setTotalBus(totalBus);
        dailyBusStats.setTotalAmount(totalAmount);
        dailyBusStats.setTotalVendor(totalVendor);
        dailyBusStats.setTotalPassengers(totalPassengers);
        dailyBusStats.setAverageAmount(averageAmount);
        dailyBusStats.setAvPassengerPerBus(avPassengerPerBus);
        dailyBusStats.setTotalTime(totalTime);
        dailyBusStats.setAvJourneyTime(avJourneyTime);
        return dailyBusStats;
    }
}
