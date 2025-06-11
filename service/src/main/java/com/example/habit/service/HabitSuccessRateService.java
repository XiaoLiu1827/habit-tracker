package com.example.habit.service;

import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.stereotype.Service;

import com.example.habit.dto.SuccessRateDto;
import com.example.habit.model.view.SuccessRateStat;
import com.example.habit.repository.HabitRepository;
import com.example.habit.repository.ReviewRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitSuccessRateService {

	private final ReviewRecordRepository reviewRecordRepository;
	private final HabitRepository habitRepository;

	public SuccessRateDto getSuccessRate(Long habitId, YearMonth month) {
		LocalDate start = month.atDay(1);
		LocalDate end = month.atEndOfMonth();

		SuccessRateStat stat = reviewRecordRepository.getSuccessRateByHabitIdAndDateRange(habitId, start, end);
		int totalCount = (stat != null) ? stat.totalCount().intValue() : 0;
		int successCount = (stat != null) ? stat.successCount().intValue() : 0;
		int successRate = (totalCount == 0) ? 0 : (int) Math.round((successCount * 100.0) / totalCount);

		return new SuccessRateDto(successRate, successCount, totalCount);

	}
}
