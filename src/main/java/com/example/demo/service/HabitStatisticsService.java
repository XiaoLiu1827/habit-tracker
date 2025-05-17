package com.example.demo.service;

import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.stereotype.Service;

import com.example.demo.dto.SuccessRateResponse;
import com.example.demo.dto.SuccessRateStat;
import com.example.demo.repository.HabitRepository;
import com.example.demo.repository.ReviewRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitStatisticsService {
    private final ReviewRecordRepository reviewRecordRepository;
    private final HabitRepository habitRepository;
    
    public SuccessRateResponse getSuccessRate(Long habitId, YearMonth month) {
    	LocalDate start = month.atDay(1);
    	LocalDate end = month.atEndOfMonth();
    	
    	SuccessRateStat stat = reviewRecordRepository.getSuccessRateByHabitIdAndDateRange(habitId, start, end);
        int total = (stat != null) ? stat.totalCount().intValue(): 0;
        int success = (stat != null) ? stat.successCount().intValue() : 0;
        int rate = (total == 0) ? 0 : (int) Math.round((success * 100.0) / total);

        return new SuccessRateResponse(habitId, rate, success, total);

    }
}
