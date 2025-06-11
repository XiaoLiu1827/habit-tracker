package com.example.habit.dto;

public record HabitStatisticsResponse(
		Long habitId,
		SuccessRateDto successRateDto,
		StreakDto streakDto
) {
}