package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ReviewAnswer;
import com.example.demo.model.ReviewRecord;

public interface ReviewAnswerRepository extends JpaRepository<ReviewAnswer, Long> {
    List<ReviewAnswer> findByReviewRecord(ReviewRecord reviewRecord);
}