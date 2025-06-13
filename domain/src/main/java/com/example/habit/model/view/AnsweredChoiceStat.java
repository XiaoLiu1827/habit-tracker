package com.example.habit.model.view;

public record AnsweredChoiceStat(
		Long choiceId,
		String choiceLabel,
		Long questionId,
		String questionLabel,
		Long answerCount) {

}
