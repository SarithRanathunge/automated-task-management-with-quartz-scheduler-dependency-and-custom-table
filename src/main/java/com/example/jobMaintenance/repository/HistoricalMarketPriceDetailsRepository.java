package com.example.jobMaintenance.repository;

import com.example.jobMaintenance.model.HistoricalMarketPriceDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricalMarketPriceDetailsRepository extends JpaRepository<HistoricalMarketPriceDetails, Integer> {
}
