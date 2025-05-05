package com.example.demo.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ReviewSession;

public interface ReviewSessionRepository extends JpaRepository<ReviewSession, Long> {
    boolean existsByUserIdAndReviewDate(Long userId, LocalDate reviewDate);
}
