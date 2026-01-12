package com.example.jobMaintenance.controller;

import com.example.jobMaintenance.dto.CurrentMarketPriceRequestDto;
import com.example.jobMaintenance.dto.CurrentMarketPriceResponseDto;
import com.example.jobMaintenance.dto.HistoricalMarketPriceResponseDto;
import com.example.jobMaintenance.service.MarketPricesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market-prices")
@RequiredArgsConstructor
public class MarketPriceController {

    private final MarketPricesService service;

    // GET API 1: Fetch all historical market price records
    @GetMapping("/historical")
    public ResponseEntity<List<HistoricalMarketPriceResponseDto>> getAllHistoricalPrices() {
        List<HistoricalMarketPriceResponseDto> prices = service.getAllHistoricalPrices();
        return ResponseEntity.ok(prices);
    }

    // GET API 2: Fetch a single record by ID
    @GetMapping("/historical/{id}")
    public ResponseEntity<HistoricalMarketPriceResponseDto> getHistoricalPriceById(@PathVariable Integer id) {
        HistoricalMarketPriceResponseDto price = service.getHistoricalPriceById(id);
        return ResponseEntity.ok(price);
    }

    // GET all
    @GetMapping("/current")
    public ResponseEntity<List<CurrentMarketPriceResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET by ID
    @GetMapping("/current/{id}")
    public ResponseEntity<CurrentMarketPriceResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // POST - create new
    @PostMapping("/current")
    public ResponseEntity<CurrentMarketPriceResponseDto> create(
            @Valid @RequestBody CurrentMarketPriceRequestDto requestDto) {
        CurrentMarketPriceResponseDto response = service.create(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT - update existing
    @PutMapping("/current/{id}")
    public ResponseEntity<CurrentMarketPriceResponseDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody CurrentMarketPriceRequestDto requestDto) {
        CurrentMarketPriceResponseDto response = service.update(id, requestDto);
        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/current/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}