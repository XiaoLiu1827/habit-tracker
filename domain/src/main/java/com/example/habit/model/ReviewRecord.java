package com.example.habit.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Habit habit;

	private LocalDate date;

	private boolean success;

	@ManyToOne
	private User user;

	@OneToMany(mappedBy = "reviewRecord", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReviewAnswer> answers = new ArrayList<>();
}
