package com.example.jobMaintenance.controller;

import com.example.jobMaintenance.config.HistoricalJobProperties;
import com.example.jobMaintenance.constants.JobCurrentStatus;
import com.example.jobMaintenance.constants.JobType;
import com.example.jobMaintenance.constants.LastRunStatus;
import com.example.jobMaintenance.dto.DailyJobMaintenanceResponseDTO;
import com.example.jobMaintenance.model.DailyJobMaintenanceDetails;
import com.example.jobMaintenance.repository.DailyJobMaintenanceDetailsRepository;
import com.example.jobMaintenance.service.MarketPricesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/job-maintenance")
@RequiredArgsConstructor
public class JobMaintenanceController {

    private final DailyJobMaintenanceDetailsRepository repo;
    private final HistoricalJobProperties jobProperties;
    private final MarketPricesService marketPricesService;

    private static final ZoneId ZONE = ZoneId.of("Asia/Colombo");
    private static final JobType HISTORICAL_JOB_TYPE = JobType.HISTORICAL_MARKET_PRICES_DATA_UPDATE;


    // ==================== NEW: Manually Run Today's Job ====================
    @PostMapping("/run-today-historical-job")
    public ResponseEntity<String> runTodayJobManually() {
        LocalDate today = LocalDate.now(ZONE);
        LocalDateTime nowDateTime = LocalDateTime.now(ZONE);

        DailyJobMaintenanceDetails record = repo
                .findByJobTypeAndCreatedAt(HISTORICAL_JOB_TYPE, today)
                .orElseThrow(() -> new RuntimeException("No job record found for today"));

        JobCurrentStatus currentStatus = record.getJobCurrentStatus();
        LastRunStatus lastRunStatus = record.getLastRunStatus();

        // Check allowance based on rules
        boolean allowed = false;
        if (currentStatus == JobCurrentStatus.COMPLETED && lastRunStatus == LastRunStatus.FAILURE) {
            allowed = true;
        } else if (currentStatus == JobCurrentStatus.PENDING && lastRunStatus == LastRunStatus.NOT_DONE) {
            allowed = true;
        }
        // STOPPED: Not allowed until activated
        // COMPLETED / SUCCESS: Not allowed

        if (!allowed) {
            return ResponseEntity.badRequest()
                    .body("Job cannot be run manually. Current status: " + currentStatus +
                            " / " + lastRunStatus);
        }

        // Run the job
        try {
            marketPricesService.updateHistoricalTableWithCurrentTable();

            // Success
            record.setJobCurrentStatus(JobCurrentStatus.COMPLETED);
            record.setLastRunStatus(LastRunStatus.SUCCESS);
            record.setJobLastDate(nowDateTime.toLocalDate());
            record.setJobLastTime(nowDateTime.toLocalTime());

        } catch (Exception e) {
            // Failure
            record.setJobCurrentStatus(JobCurrentStatus.COMPLETED);
            record.setLastRunStatus(LastRunStatus.FAILURE);
            record.setJobLastDate(nowDateTime.toLocalDate());
            record.setJobLastTime(nowDateTime.toLocalTime());
        }

        repo.save(record);

        return ResponseEntity.ok(
                "Manual run completed. New status: " + record.getJobCurrentStatus() +
                        " / " + record.getLastRunStatus()
        );
    }

    // ==================== NEW: Activate Stopped Job ====================
    @PostMapping("/activate-today-historical-job")
    public ResponseEntity<String> activateTodayJob() {
        LocalDate today = LocalDate.now(ZONE);
        LocalTime now = LocalTime.now(ZONE);
        LocalTime scheduledTime = jobProperties.getScheduledLocalTime(); // From properties

        DailyJobMaintenanceDetails record = repo
                .findByJobTypeAndCreatedAt(HISTORICAL_JOB_TYPE, today)
                .orElseThrow(() -> new RuntimeException("No job record found for today"));

        if (record.getJobCurrentStatus() != JobCurrentStatus.STOPPED) {
            return ResponseEntity.badRequest()
                    .body("Job cannot be activated. Current status: " + record.getJobCurrentStatus());
        }

        LocalDateTime scheduledDateTime = LocalDateTime.of(today, scheduledTime);
        LocalDateTime nowDateTime = LocalDateTime.now(ZONE);

        if (nowDateTime.isAfter(scheduledDateTime) || nowDateTime.equals(scheduledDateTime)) {
            // Time has passed → mark as missed
            record.setJobCurrentStatus(JobCurrentStatus.COMPLETED);
            record.setLastRunStatus(LastRunStatus.NOT_DONE);
            record.setJobLastDate(null);
            record.setJobLastTime(null);
        } else {
            // Time not passed → can still run
            record.setJobCurrentStatus(JobCurrentStatus.PENDING);
            record.setLastRunStatus(LastRunStatus.NOT_DONE);
        }

        repo.save(record);

        return ResponseEntity.ok(
                "Today's historical market prices job has been activated. " +
                        "New status: " + record.getJobCurrentStatus() +
                        " / " + record.getLastRunStatus()
        );
    }


    // ==================== Existing: Stop Today's Job ====================
    @PostMapping("/stop-today-historical-job")
    public ResponseEntity<String> stopTodayJob() {
        LocalDate today = LocalDate.now(ZONE);

        DailyJobMaintenanceDetails record = repo
                .findByJobTypeAndCreatedAt(HISTORICAL_JOB_TYPE, today)
                .orElseThrow(() -> new RuntimeException("No job record found for today"));

        if (record.getJobCurrentStatus() != JobCurrentStatus.PENDING) {
            return ResponseEntity.badRequest().body("Job is not in pending state. Current: " + record.getJobCurrentStatus());
        }

        record.setJobCurrentStatus(JobCurrentStatus.STOPPED);
        record.setLastRunStatus(LastRunStatus.NOT_DONE);
        // jobLastDate and jobLastTime remain null
        repo.save(record);

        return ResponseEntity.ok("Today's historical market prices job has been stopped");
    }

    // ==================== 1. Today's Records (All Job Types) ====================
    @GetMapping("/today")
    public ResponseEntity<List<DailyJobMaintenanceResponseDTO>> getTodayAllJobRecords() {
        LocalDate today = LocalDate.now(ZONE);

        List<DailyJobMaintenanceDetails> records = repo.findByCreatedAt(today);

        List<DailyJobMaintenanceResponseDTO> dtos = records.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // ==================== 2. Previous Records for Specific Job Type ====================
    @GetMapping("/history/{jobType}")
    public ResponseEntity<List<DailyJobMaintenanceResponseDTO>> getPreviousRecordsByJobType(
            @PathVariable String jobType) {

        JobType type = JobType.fromValue(jobType); // Validates and converts

        LocalDate today = LocalDate.now(ZONE);

        List<DailyJobMaintenanceDetails> records = repo.findByJobTypeAndCreatedAtBefore(type, today);

        List<DailyJobMaintenanceResponseDTO> dtos = records.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // ==================== 3. All Available Job Types ====================
    @GetMapping("/job-types")
    public ResponseEntity<List<String>> getAllJobTypes() {
        List<String> jobTypeDisplays = Arrays.stream(JobType.values())
                .map(JobType::getDisplayName)
                .collect(Collectors.toList());

        return ResponseEntity.ok(jobTypeDisplays);
    }


    // ==================== Helper: Entity → DTO ====================
    private DailyJobMaintenanceResponseDTO mapToDTO(DailyJobMaintenanceDetails entity) {
        return DailyJobMaintenanceResponseDTO.builder()
                .dailyJobMaintenanceDetailsId(entity.getDailyJobMaintenanceDetailsId())
                .jobType(entity.getJobType())
                .jobCurrentStatus(entity.getJobCurrentStatus())
                .jobDescription(entity.getJobDescription())
                .jobLastDate(entity.getJobLastDate())
                .jobLastTime(entity.getJobLastTime())
                .lastRunStatus(entity.getLastRunStatus())
                .createdAt(entity.getCreatedAt())
                .createdTime(entity.getCreatedTime())
                .build();
    }
}