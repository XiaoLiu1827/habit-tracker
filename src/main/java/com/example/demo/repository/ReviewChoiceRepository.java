package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ReviewChoiceMaster;

public interface ReviewChoiceRepository extends JpaRepository<ReviewChoiceMaster, Long> {
	List<ReviewChoiceMaster> findByQuestionId(Long questionId);
}
