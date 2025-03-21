package com.infinite.busTicket.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllTimeStats {
    private Long totalBus;
    private Long totalVendor;
    private Long totalPassenger;
    private Long totalTicket;
    private Long bookedTicket;
    private Long paidTicket;
    private Float totalAmount;
    private Float averageAmountPerTicket;
    private Float averageAmountPerBus;
}
