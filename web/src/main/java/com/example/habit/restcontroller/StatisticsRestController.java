package com.example.habit.restcontroller;

import java.time.YearMonth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.habit.dto.HabitStatisticsResponse;
import com.example.habit.service.HabitStatisticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsRestController {
    private final HabitStatisticsService statisticsService;

    @GetMapping
    public HabitStatisticsResponse getSuccessRate(
        @RequestParam Long habitId,
        @RequestParam int year,
        @RequestParam int month
    ) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return statisticsService.getStatics(habitId, yearMonth);
    }
}
