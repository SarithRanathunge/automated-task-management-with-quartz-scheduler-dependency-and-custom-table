package com.example.jobMaintenance.repository;

import com.example.jobMaintenance.constants.JobType;
import com.example.jobMaintenance.model.DailyJobMaintenanceDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyJobMaintenanceDetailsRepository
        extends JpaRepository<DailyJobMaintenanceDetails, Integer> {

    Optional<DailyJobMaintenanceDetails> findByJobTypeAndCreatedAt(JobType jobType, LocalDate createdAt);

    boolean existsByJobTypeAndCreatedAt(JobType jobType, LocalDate createdAt);

    // New methods for APIs
    List<DailyJobMaintenanceDetails> findByCreatedAt(LocalDate createdAt);

    List<DailyJobMaintenanceDetails> findByJobTypeAndCreatedAtBefore(JobType jobType, LocalDate date);
}