package com.example.jobMaintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricalMarketPriceResponseDto {

    private Integer historicalMarketPriceDetailsId;
    private LocalDate date;
    private BigDecimal previousClose;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private LocalTime archivedUpdatedTime;
    private LocalDate archivedUpdatedDate;
    private String archivedUpdatedBy;
}