package com.example.jobMaintenance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentMarketPriceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer currentMarketPriceDetailsId;
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
    @UpdateTimestamp
    private LocalTime lastUpdatedTime;
    @UpdateTimestamp
    private LocalDate lastUpdatedDate;
    private String lastUpdatedBy;
}
