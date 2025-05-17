package com.example.demo.dto;
//DBマッピング用
public record SuccessRateStat(
		Long habitId,
		Long totalCount,//sumやcountはBIGINTになるためLongにして合わせる
		Long successCount) {
}
