package com.example.habit.dto;

import java.util.List;

public record HabitStatisticsResponse(
		Long habitId,
		SuccessRateDto successRateDto,
		StreakDto streakDto,
		List<QuestionStatDto> questionStatList
) {
}