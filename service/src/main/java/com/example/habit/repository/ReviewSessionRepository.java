package com.example.habit.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.habit.model.ReviewSession;

public interface ReviewSessionRepository extends JpaRepository<ReviewSession, Long> {
    boolean existsByUserIdAndReviewDate(Long userId, LocalDate reviewDate);
}
