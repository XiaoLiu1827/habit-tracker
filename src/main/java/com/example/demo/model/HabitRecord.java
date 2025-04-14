package com.example.demo.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class HabitRecord {
	private Long id;
	private Long habitId; // 紐づく習慣
	private LocalDate date; // 記録日
	private boolean success; // 成功か失敗か
}
