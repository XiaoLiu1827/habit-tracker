package com.example.demo.dto;

public record SuccessRateResponse(
		Long habitId,
		int successRate,
		int successCount,
		int totalCount) {
}