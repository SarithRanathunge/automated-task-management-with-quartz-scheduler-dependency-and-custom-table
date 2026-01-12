package com.example.jobMaintenance.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentMarketPriceRequestDto {
    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Previous close is required")
    @Digits(integer = 22, fraction = 2)
    private BigDecimal previousClose;

    @NotNull(message = "Open price is required")
    @Digits(integer = 22, fraction = 2)
    private BigDecimal open;

    @NotNull(message = "High price is required")
    @Digits(integer = 22, fraction = 2)
    private BigDecimal high;

    @NotNull(message = "Low price is required")
    @Digits(integer = 22, fraction = 2)
    private BigDecimal low;

    @NotNull(message = "Close price is required")
    @Digits(integer = 22, fraction = 2)
    private BigDecimal close;

    private String lastUpdatedBy;
}
