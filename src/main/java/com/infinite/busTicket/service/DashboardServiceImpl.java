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

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DailyBusStats getDailyBusStats(String date) {
        LocalDate selectedDate = LocalDate.parse(date);

        // Filter bus
        List<BusEntity> filteredBuses = busRepository.findAll().stream()
                .filter(bus -> bus.getDateOfJourney().isEqual(selectedDate))
                .toList();

        DailyBusStats stats = new DailyBusStats();
        if (filteredBuses.isEmpty()) {
            return stats;
        }

        Set<String> vendors = filteredBuses.stream()
                .map(BusEntity::getVendorName)
                .collect(Collectors.toSet());

        long totalPassengers = 0;
        float totalAmount = 0;
        Duration totalDuration = Duration.ZERO;

        for (BusEntity bus : filteredBuses) {
            for (TicketEntity ticket : bus.getTickets()) {
                totalPassengers += ticket.getPassengers().size();
                totalAmount += ticket.getAmount();
            }
            totalDuration = totalDuration.plus(Duration.between(
                    bus.getTimeOfDropping(), bus.getTimeOfBoarding()));
        }

        stats.setTotalBus((long) filteredBuses.size());
        stats.setTotalVendor((long) vendors.size());
        stats.setTotalPassengers(totalPassengers);
        stats.setTotalAmount(totalAmount);
        stats.setTotalTime(totalDuration);

        stats.setAvPassengerPerBus((float) totalPassengers / filteredBuses.size());
        stats.setAvJourneyTime(totalDuration.dividedBy(filteredBuses.size()));
        stats.setAverageAmount(totalAmount / filteredBuses.size());

        return stats;
    }

    @Override
    public DailyTicketStats getDailyTicketStats(String date) {
        LocalDate localDate = LocalDate.parse(date);

        // Filter tickets for the selected date
        List<TicketEntity> filteredTickets = ticketRepository.findAll().stream()
                .filter(ticket -> ticket.getBookingTime().toLocalDate().isEqual(localDate))
                .toList();

        DailyTicketStats stats = new DailyTicketStats();
        if (filteredTickets.isEmpty()) {
            return stats;
        }

        long bookedTickets = 0;
        long paidTickets = 0;
        float totalAmount = 0;

        for (TicketEntity ticket : filteredTickets) {
            if (ticket.getPaymentTime() == null && ticket.getBookingTime() != null) {
                bookedTickets++;
            } else {
                paidTickets++;
            }
            totalAmount += ticket.getAmount();
        }

        stats.setTotalTicket((long) filteredTickets.size());
        stats.setBookedTicket(bookedTickets);
        stats.setPaidTicket(paidTickets);
        stats.setTotalAmount(totalAmount);

        if (paidTickets > 0) {
            stats.setAverageAmount(totalAmount / paidTickets);
        } else {
            stats.setAverageAmount(0f);
        }

        return stats;
    }

    @Override
    public AllTimeStats getStats() {
        List<TicketEntity> tickets = ticketRepository.findAll();
        List<BusEntity> buses = busRepository.findAll();

        AllTimeStats stats = new AllTimeStats();

        if (tickets.isEmpty() && buses.isEmpty()) {
            return stats;
        }

        long bookedTickets = 0;
        long paidTickets = 0;
        long totalPassengers = 0;
        float totalAmount = 0;

        for (TicketEntity ticket : tickets) {
            if (ticket.getPaymentTime() == null && ticket.getBookingTime() != null) {
                bookedTickets++;
            } else {
                paidTickets++;
            }
            totalPassengers += ticket.getPassengers().size();
            totalAmount += ticket.getAmount();
        }

        Set<String> vendors = buses.stream()
                .map(BusEntity::getVendorName)
                .collect(Collectors.toSet());

        stats.setTotalBus((long) buses.size());
        stats.setTotalTicket((long) tickets.size());
        stats.setBookedTicket(bookedTickets);
        stats.setPaidTicket(paidTickets);
        stats.setTotalPassenger(totalPassengers);
        stats.setTotalAmount(totalAmount);
        stats.setTotalVendor((long) vendors.size());

        if (!buses.isEmpty()) {
            stats.setAverageAmountPerBus(totalAmount / buses.size());
        }

        if (paidTickets > 0) {
            stats.setAverageAmountPerTicket(totalAmount / paidTickets);
        }

        return stats;
    }

    @Override
    public AllTimeStats getStatsByConductorId(Long id) {
        Optional<Users> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return new AllTimeStats();
        }

        List<BusEntity> buses = userOptional.get().getBuses();
        if (buses.isEmpty()) {
            return new AllTimeStats();
        }

        // Collect unique vendors
        Set<String> vendors = buses.stream()
                .map(BusEntity::getVendorName)
                .collect(Collectors.toSet());

        long totalTickets = 0;
        long bookedTickets = 0;
        long paidTickets = 0;
        long totalPassengers = 0;
        float totalAmount = 0;

        for (BusEntity bus : buses) {
            List<TicketEntity> tickets = bus.getTickets();
            totalTickets += tickets.size();

            for (TicketEntity ticket : tickets) {
                if (ticket.getPaymentTime() == null && ticket.getBookingTime() != null) {
                    bookedTickets++;
                } else {
                    paidTickets++;
                }
                totalPassengers += ticket.getPassengers().size();
                totalAmount += ticket.getAmount();
            }
        }

        AllTimeStats stats = new AllTimeStats();
        stats.setTotalBus((long) buses.size());
        stats.setTotalTicket(totalTickets);
        stats.setBookedTicket(bookedTickets);
        stats.setPaidTicket(paidTickets);
        stats.setTotalPassenger(totalPassengers);
        stats.setTotalAmount(totalAmount);
        stats.setTotalVendor((long) vendors.size());

        if (!buses.isEmpty()) {
            stats.setAverageAmountPerBus(totalAmount / buses.size());
        }

        if (totalTickets > 0) {
            stats.setAverageAmountPerTicket(totalAmount / totalTickets);
        }

        return stats;
    }

    @Override
    public DailyTicketStats getDailyTicketStatsByConductorId(Long id, String date) {
        LocalDate searchDate = LocalDate.parse(date);
        Optional<Users> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return new DailyTicketStats();
        }

        DailyTicketStats stats = new DailyTicketStats();
        List<BusEntity> buses = userOptional.get().getBuses();
        if (buses.isEmpty()) {
            return stats;
        }

        long totalTickets = 0;
        long bookedTickets = 0;
        long paidTickets = 0;
        float totalAmount = 0;

        for (BusEntity bus : buses) {
            for (TicketEntity ticket : bus.getTickets()) {
                if (ticket.getBookingTime().toLocalDate().isEqual(searchDate)) {
                    totalTickets++;

                    if (ticket.getPaymentTime() == null) {
                        bookedTickets++;
                    } else {
                        paidTickets++;
                    }

                    totalAmount += ticket.getAmount();
                }
            }
        }

        stats.setTotalTicket(totalTickets);
        stats.setBookedTicket(bookedTickets);
        stats.setPaidTicket(paidTickets);
        stats.setTotalAmount(totalAmount);

        if (totalTickets > 0) {
            stats.setAverageAmount(totalAmount / totalTickets);
        } else {
            stats.setAverageAmount(0f);
        }

        return stats;
    }

    @Override
    public DailyBusStats getDailyBusStatsByConductorId(Long id, String date) {
        LocalDate searchDate = LocalDate.parse(date);
        Optional<Users> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return new DailyBusStats();
        }

        List<BusEntity> filteredBuses = userOptional.get().getBuses().stream()
                .filter(bus -> bus.getDateOfJourney().isEqual(searchDate))
                .toList();

        DailyBusStats stats = new DailyBusStats();
        if (filteredBuses.isEmpty()) {
            return stats;
        }

        Set<String> vendors = filteredBuses.stream()
                .map(BusEntity::getVendorName)
                .collect(Collectors.toSet());

        long totalPassengers = 0;
        float totalAmount = 0;
        Duration totalDuration = Duration.ZERO;

        for (BusEntity bus : filteredBuses) {
            for (TicketEntity ticket : bus.getTickets()) {
                totalPassengers += ticket.getPassengers().size();
                totalAmount += ticket.getAmount();
            }
            totalDuration = totalDuration.plus(Duration.between(
                    bus.getTimeOfDropping(), bus.getTimeOfBoarding()));
        }

        stats.setTotalBus((long) filteredBuses.size());
        stats.setTotalVendor((long) vendors.size());
        stats.setTotalPassengers(totalPassengers);
        stats.setTotalAmount(totalAmount);
        stats.setTotalTime(totalDuration);

        if (!filteredBuses.isEmpty()) {
            stats.setAverageAmount(totalAmount / filteredBuses.size());
            stats.setAvPassengerPerBus((float) totalPassengers / filteredBuses.size());
            stats.setAvJourneyTime(totalDuration.dividedBy(filteredBuses.size()));
        }

        return stats;
    }
}