package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.dto.QuestionWithChoicesDto;
import com.example.demo.model.HabitType;
import com.example.demo.model.ReviewQuestionMaster;
import com.example.demo.model.ReviewType;
import com.example.demo.repository.ReviewQuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewQuestionService {

	private final ReviewQuestionRepository questionRepository;
	
	//習慣タイプと結果区分で分類された質問群を返す
	public Map<String, List<QuestionWithChoicesDto>> getGroupedQuestions() {
		Map<String, List<QuestionWithChoicesDto>> result = new HashMap<>();

		for (HabitType habitType : HabitType.values()) {
			for (ReviewType reviewType : ReviewType.values()) {
				String key = habitType.name().toLowerCase() + "_" + reviewType.name().toLowerCase(); // 例: continue_success
				List<ReviewQuestionMaster> questions = questionRepository.findByHabitTypeAndReviewType(habitType,
						reviewType);
				List<QuestionWithChoicesDto> dtoList = questions.stream()
						.map(QuestionWithChoicesDto::from)
						.toList();

				result.put(key, dtoList);
			}
		}

		return result;
	}
}
