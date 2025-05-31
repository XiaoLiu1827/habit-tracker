package com.example.habit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.habit.model.HabitType;
import com.example.habit.model.ReviewQuestionMaster;
import com.example.habit.model.ReviewType;

public interface ReviewQuestionRepository extends JpaRepository<ReviewQuestionMaster, Long> {
    List<ReviewQuestionMaster> findByHabitTypeAndReviewType(HabitType habitType, ReviewType reviewType);
}