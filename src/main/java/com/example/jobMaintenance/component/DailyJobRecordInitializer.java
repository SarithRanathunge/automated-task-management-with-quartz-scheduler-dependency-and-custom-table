package com.example.jobMaintenance.component;

import com.example.jobMaintenance.constants.JobCurrentStatus;
import com.example.jobMaintenance.constants.JobType;
import com.example.jobMaintenance.constants.LastRunStatus;
import com.example.jobMaintenance.model.DailyJobMaintenanceDetails;
import com.example.jobMaintenance.repository.DailyJobMaintenanceDetailsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class DailyJobRecordInitializer {

    private final DailyJobMaintenanceDetailsRepository repo;

    private static final ZoneId ZONE = ZoneId.of("Asia/Colombo");
    private static final JobType JOB_TYPE = JobType.HISTORICAL_MARKET_PRICES_DATA_UPDATE;

    public DailyJobRecordInitializer(DailyJobMaintenanceDetailsRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void initializeTodayJobRecord() {
        LocalDate today = LocalDate.now(ZONE);
        LocalTime now = LocalTime.now(ZONE);

        boolean exists = repo.existsByJobTypeAndCreatedAt(JOB_TYPE, today);

        if (!exists) {
            DailyJobMaintenanceDetails newRecord = DailyJobMaintenanceDetails.builder()
                    .jobType(JOB_TYPE)
                    .jobCurrentStatus(JobCurrentStatus.PENDING)
                    .lastRunStatus(LastRunStatus.NOT_DONE)
                    .jobDescription("Daily archiving of current market prices to historical table")
                    .jobLastDate(null)
                    .jobLastTime(null)
                    .createdAt(today)           // This identifies the day
                    .createdTime(now)           // Time when record was created
                    .build();

            repo.save(newRecord);
        }
        // If record exists → preserve state (PENDING, STOPPED, COMPLETED)
    }
}