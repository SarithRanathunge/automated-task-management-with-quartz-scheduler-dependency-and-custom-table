package com.example.jobMaintenance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricalMarketPriceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historicalMarketPriceDetailsId;
    private LocalDate date;
    @Column(precision = 24, scale = 2, nullable = false)
    private BigDecimal previousClose;
    @Column(precision = 24, scale = 2, nullable = false)
    private BigDecimal open;
    @Column(precision = 24, scale = 2, nullable = false)
    private BigDecimal high;
    @Column(precision = 24, scale = 2, nullable = false)
    private BigDecimal low;
    @Column(precision = 24, scale = 2, nullable = false)
    private BigDecimal close;
    private LocalTime archivedUpdatedTime;
    private LocalDate archivedUpdatedDate;
    private String archivedUpdatedBy;
}
