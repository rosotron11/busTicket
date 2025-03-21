package com.infinite.busTicket.service;
import com.infinite.busTicket.entity.response.AllTimeStats;
import com.infinite.busTicket.entity.response.DailyBusStats;
import com.infinite.busTicket.entity.response.DailyTicketStats;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    DailyBusStats getDailyBusStats(String date);

    DailyTicketStats getDailyTicketStats(String date);

    AllTimeStats getStats();

    AllTimeStats getStatsByConductorId(Long id);

    DailyTicketStats getDailyTicketStatsByConductorId(Long id, String date);

    DailyBusStats getDailyBusStatsByConductorId(Long id, String date);
}
