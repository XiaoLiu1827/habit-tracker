package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.HabitType;
import com.example.demo.model.ReviewQuestionMaster;
import com.example.demo.model.ReviewType;

public interface ReviewQuestionRepository extends JpaRepository<ReviewQuestionMaster, Long> {
    List<ReviewQuestionMaster> findByHabitTypeAndReviewType(HabitType habitType, ReviewType reviewType);
}