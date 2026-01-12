package com.example.jobMaintenance.component;

import com.example.jobMaintenance.constants.JobCurrentStatus;
import com.example.jobMaintenance.constants.JobType;
import com.example.jobMaintenance.constants.LastRunStatus;
import com.example.jobMaintenance.model.DailyJobMaintenanceDetails;
import com.example.jobMaintenance.repository.DailyJobMaintenanceDetailsRepository;
import com.example.jobMaintenance.service.MarketPricesService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class HistoricalMarketPricesJob implements Job {

    private final MarketPricesService marketPricesService;
    private final DailyJobMaintenanceDetailsRepository maintenanceRepo;

    private static final ZoneId ZONE = ZoneId.of("Asia/Colombo");
    private static final JobType JOB_TYPE = JobType.HISTORICAL_MARKET_PRICES_DATA_UPDATE;

    @Override
    public void execute(JobExecutionContext context) {
        LocalDate today = LocalDate.now(ZONE);

        DailyJobMaintenanceDetails todayRecord = maintenanceRepo
                .findByJobTypeAndCreatedAt(JOB_TYPE, today)
                .orElse(null);

        // If no record or not PENDING → skip execution
        if (todayRecord == null || todayRecord.getJobCurrentStatus() != JobCurrentStatus.PENDING) {
            return;
        }

        LocalDate runDate = LocalDate.now(ZONE);
        LocalTime runTime = LocalTime.now(ZONE);

        try {
            marketPricesService.updateHistoricalTableWithCurrentTable();

            // Success
            todayRecord.setJobCurrentStatus(JobCurrentStatus.COMPLETED);
            todayRecord.setLastRunStatus(LastRunStatus.SUCCESS);
            todayRecord.setJobLastDate(runDate);
            todayRecord.setJobLastTime(runTime);

        } catch (Exception e) {
            // Failure
            todayRecord.setJobCurrentStatus(JobCurrentStatus.COMPLETED);
            todayRecord.setLastRunStatus(LastRunStatus.FAILURE);
            todayRecord.setJobLastDate(runDate);
            todayRecord.setJobLastTime(runTime);
        }

        maintenanceRepo.save(todayRecord);
    }
}