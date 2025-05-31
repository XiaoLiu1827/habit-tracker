package com.example.habit.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor 
public class Habit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String label;
	
	private String description;
	
	private LocalDate createdDate;
	
	@Enumerated(EnumType.STRING)
	private HabitType type; // CONTINUE（続けたい） / QUIT（やめたい）
	
	private boolean isActive = true;
	
	@ManyToOne
	private User user;

	public Habit(Long id, String label) {
		this.id = id;
		this.label = label;
		this.createdDate = LocalDate.now();
	}
}
