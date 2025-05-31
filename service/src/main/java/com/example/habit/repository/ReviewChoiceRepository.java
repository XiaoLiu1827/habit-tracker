package com.example.habit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.habit.model.ReviewChoiceMaster;

public interface ReviewChoiceRepository extends JpaRepository<ReviewChoiceMaster, Long> {
	List<ReviewChoiceMaster> findByQuestionId(Long questionId);
}
