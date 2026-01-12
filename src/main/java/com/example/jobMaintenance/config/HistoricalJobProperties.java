package com.example.jobMaintenance.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@ConfigurationProperties(prefix = "job.historical-market-prices")
@Validated
@Getter
@Setter
public class HistoricalJobProperties {

    @NotBlank
    private String scheduledTime; // e.g., "22:00"

    // Parsed LocalTime for easy use in code
    public LocalTime getScheduledLocalTime() {
        return LocalTime.parse(scheduledTime, DateTimeFormatter.ofPattern("HH:mm"));
    }
}