package com.infinite.busTicket.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyTicketStats {
    private Long totalTicket;
    private Long bookedTicket;
    private Long paidTicket;
    private Float totalAmount;
    private Float averageAmount;
}
