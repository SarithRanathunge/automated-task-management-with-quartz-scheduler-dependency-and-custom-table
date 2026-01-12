package com.example.jobMaintenance.dto;

import com.example.jobMaintenance.constants.JobCurrentStatus;
import com.example.jobMaintenance.constants.JobType;
import com.example.jobMaintenance.constants.LastRunStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyJobMaintenanceResponseDTO {
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