package com.example.habit.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.habit.dto.HabitStatisticsResponse;
import com.example.habit.dto.QuestionStatDto;
import com.example.habit.dto.StreakDto;
import com.example.habit.dto.SuccessRateDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitStatisticsService {
    private final HabitSuccessRateService successRateService;
    private final HabitStreakService habitStreakService;
    private final ReviewAnswerStatService reviewAnswerStatService;
    
    public HabitStatisticsResponse getStatics(Long habitId, YearMonth month) {
    	LocalDate start = month.atDay(1);
    	LocalDate end = month.atEndOfMonth();
    	
    	//今月の成功率を取得
    	SuccessRateDto successRateDto = successRateService.getSuccessRate(habitId, month);
    	StreakDto streakDto = habitStreakService.getStreak(habitId);
    	List<QuestionStatDto> questionStatList = reviewAnswerStatService.getAnsweredChoicesGroupedByQuestion(habitId);

        return new HabitStatisticsResponse(habitId, successRateDto, streakDto, questionStatList);

    }
}
