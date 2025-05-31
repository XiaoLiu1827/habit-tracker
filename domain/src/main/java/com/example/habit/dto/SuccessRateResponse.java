package com.example.habit.dto;

public record SuccessRateResponse(
		Long habitId,
		int successRate,
		int successCount,
		int totalCount) {
}