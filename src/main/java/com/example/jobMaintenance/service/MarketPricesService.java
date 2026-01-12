package com.example.jobMaintenance.service;

import com.example.jobMaintenance.dto.CurrentMarketPriceRequestDto;
import com.example.jobMaintenance.dto.CurrentMarketPriceResponseDto;
import com.example.jobMaintenance.dto.HistoricalMarketPriceResponseDto;
import com.example.jobMaintenance.model.CurrentMarketPriceDetails;
import com.example.jobMaintenance.model.HistoricalMarketPriceDetails;
import com.example.jobMaintenance.repository.CurrentMarketPricesDetailsRepository;
import com.example.jobMaintenance.repository.HistoricalMarketPriceDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketPricesService {
    private final CurrentMarketPricesDetailsRepository currentMarketPricesDetailsRepository;
    private final HistoricalMarketPriceDetailsRepository historicalMarketPriceDetailsRepository;

    public List<HistoricalMarketPriceResponseDto> getAllHistoricalPrices() {
        return historicalMarketPriceDetailsRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public HistoricalMarketPriceResponseDto getHistoricalPriceById(Integer id) {
        HistoricalMarketPriceDetails entity = historicalMarketPriceDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historical market price not found with id: " + id));
        return toResponseDto(entity);
    }

    private HistoricalMarketPriceResponseDto toResponseDto(HistoricalMarketPriceDetails entity) {
        return HistoricalMarketPriceResponseDto.builder()
                .historicalMarketPriceDetailsId(entity.getHistoricalMarketPriceDetailsId())
                .date(entity.getDate())
                .previousClose(entity.getPreviousClose())
                .open(entity.getOpen())
                .high(entity.getHigh())
                .low(entity.getLow())
                .close(entity.getClose())
                .archivedUpdatedTime(entity.getArchivedUpdatedTime())
                .archivedUpdatedDate(entity.getArchivedUpdatedDate())
                .archivedUpdatedBy(entity.getArchivedUpdatedBy())
                .build();
    }

    public List<CurrentMarketPriceResponseDto> getAll() {
        return currentMarketPricesDetailsRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public CurrentMarketPriceResponseDto getById(Integer id) {
        CurrentMarketPriceDetails entity = findEntityById(id);
        return toResponseDto(entity);
    }

    @Transactional
    public CurrentMarketPriceResponseDto create(CurrentMarketPriceRequestDto requestDto) {
        CurrentMarketPriceDetails entity = CurrentMarketPriceDetails.builder()
                .date(requestDto.getDate())
                .previousClose(requestDto.getPreviousClose())
                .open(requestDto.getOpen())
                .high(requestDto.getHigh())
                .low(requestDto.getLow())
                .close(requestDto.getClose())
                .lastUpdatedBy(requestDto.getLastUpdatedBy())
                .build();

        CurrentMarketPriceDetails saved = currentMarketPricesDetailsRepository.save(entity);
        return toResponseDto(saved);
    }

    @Transactional
    public CurrentMarketPriceResponseDto update(Integer id, CurrentMarketPriceRequestDto requestDto) {
        CurrentMarketPriceDetails entity = findEntityById(id);

        entity.setDate(requestDto.getDate());
        entity.setPreviousClose(requestDto.getPreviousClose());
        entity.setOpen(requestDto.getOpen());
        entity.setHigh(requestDto.getHigh());
        entity.setLow(requestDto.getLow());
        entity.setClose(requestDto.getClose());
        entity.setLastUpdatedBy(requestDto.getLastUpdatedBy());

        CurrentMarketPriceDetails updated = currentMarketPricesDetailsRepository.save(entity);
        return toResponseDto(updated);
    }

    @Transactional
    public void delete(Integer id) {
        CurrentMarketPriceDetails entity = findEntityById(id);
        currentMarketPricesDetailsRepository.delete(entity);
    }

    private CurrentMarketPriceDetails findEntityById(Integer id) {
        return currentMarketPricesDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Market price record not found with id: " + id));
    }

    private CurrentMarketPriceResponseDto toResponseDto(CurrentMarketPriceDetails entity) {
        return CurrentMarketPriceResponseDto.builder()
                .currentMarketPriceDetailsId(entity.getCurrentMarketPriceDetailsId())
                .date(entity.getDate())
                .previousClose(entity.getPreviousClose())
                .open(entity.getOpen())
                .high(entity.getHigh())
                .low(entity.getLow())
                .close(entity.getClose())
                .lastUpdatedTime(entity.getLastUpdatedTime())
                .lastUpdatedDate(entity.getLastUpdatedDate())
                .lastUpdatedBy(entity.getLastUpdatedBy())
                .build();
    }

    @Transactional
    public void updateHistoricalTableWithCurrentTable() {
        LocalDate today = LocalDate.now();

        List<CurrentMarketPriceDetails> getData = currentMarketPricesDetailsRepository.findAll();
        if (getData.isEmpty()) {
            throw new RuntimeException("Current market prices are empty");
        }

        for (CurrentMarketPriceDetails entity : getData) {
            HistoricalMarketPriceDetails save = HistoricalMarketPriceDetails.builder()
                    .date(entity.getDate())
                    .previousClose(entity.getPreviousClose())
                    .open(entity.getOpen())
                    .high(entity.getHigh())
                    .low(entity.getLow())
                    .close(entity.getClose())
                    .archivedUpdatedTime(entity.getLastUpdatedTime())
                    .archivedUpdatedBy(entity.getLastUpdatedBy())
                    .archivedUpdatedDate(entity.getLastUpdatedDate())
                    .build();

            historicalMarketPriceDetailsRepository.save(save);
        }
    }
}
