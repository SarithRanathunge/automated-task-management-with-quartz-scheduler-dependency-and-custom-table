package com.example.jobMaintenance.model;

import com.example.jobMaintenance.constants.JobCurrentStatus;
import com.example.jobMaintenance.constants.JobType;
import com.example.jobMaintenance.constants.LastRunStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyJobMaintenanceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dailyJobMaintenanceDetailsId;
    private JobType jobType;
    private JobCurrentStatus jobCurrentStatus;
    private String jobDescription;
    private LocalDate jobLastDate;
    private LocalTime jobLastTime;
    private LastRunStatus lastRunStatus;
    private LocalDate createdAt;
    private LocalTime createdTime;
}
