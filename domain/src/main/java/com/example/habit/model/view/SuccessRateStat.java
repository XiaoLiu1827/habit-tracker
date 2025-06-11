package com.example.habit.model.view;
//DBマッピング用
public record SuccessRateStat(
		Long habitId,
		Long totalCount,//sumやcountはBIGINTになるためLongにして合わせる
		Long successCount) {
}
