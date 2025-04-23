package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.ReviewQuestionMaster;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionWithChoicesDto {

	private Long id;
	private String text;
	private List<ChoiceDto> options;

	public static QuestionWithChoicesDto from(ReviewQuestionMaster question) {
		List<ChoiceDto> options = question.getChoices().stream()
				.map(c -> new ChoiceDto(c.getId(), c.getChoiceLabel()))
				.toList();
		return new QuestionWithChoicesDto(question.getId(), question.getQuestionLabel(), options);
	}

	@Data
	@AllArgsConstructor
	public static class ChoiceDto {
		private Long id;
		private String label;
	}
}
