package com.example.jobMaintenance.repository;

import com.example.jobMaintenance.model.CurrentMarketPriceDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentMarketPricesDetailsRepository extends JpaRepository<CurrentMarketPriceDetails, Integer> {
}
