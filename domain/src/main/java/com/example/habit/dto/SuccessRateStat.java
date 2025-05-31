package com.example.habit.dto;
//DBマッピング用
public record SuccessRateStat(
		Long habitId,
		Long totalCount,//sumやcountはBIGINTになるためLongにして合わせる
		Long successCount) {
}
