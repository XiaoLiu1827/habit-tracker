package com.example.habit.dto;

import java.util.List;

public record QuestionStatDto(
		Long questionId,
		String questionLabel,
		List<ChoiceStatDto> choices) {
}
