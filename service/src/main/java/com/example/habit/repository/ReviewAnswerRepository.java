package com.example.habit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.habit.model.ReviewAnswer;
import com.example.habit.model.ReviewRecord;

public interface ReviewAnswerRepository extends JpaRepository<ReviewAnswer, Long> {
    List<ReviewAnswer> findByReviewRecord(ReviewRecord reviewRecord);
}