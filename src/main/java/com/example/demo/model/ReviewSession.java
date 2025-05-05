package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_session", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "user_id", "review_date" })
})
@Data
@NoArgsConstructor
public class ReviewSession {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	//振り返り対象の日時
	@Column(name = "review_date", nullable = false)
	private LocalDate reviewDate;

	//記録日時
	@Column(name = "completed_at", nullable = false)
	private LocalDateTime completedAt;

	public ReviewSession(Long userId, LocalDate reviewDate, LocalDateTime completedAt) {
		this.userId = userId;
		this.reviewDate = reviewDate;
		this.completedAt = completedAt;
	}
}
