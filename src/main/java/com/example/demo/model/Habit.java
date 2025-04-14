package com.example.demo.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Habit {
	private Long id;
	private String name;
	private LocalDate createdDate;

	public Habit(Long id, String name) {
		this.id = id;
		this.name = name;
		this.createdDate = LocalDate.now();
	}
}
