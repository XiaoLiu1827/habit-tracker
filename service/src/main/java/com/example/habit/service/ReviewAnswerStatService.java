package com.example.habit.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.habit.dto.ChoiceStatDto;
import com.example.habit.dto.QuestionStatDto;
import com.example.habit.model.view.AnsweredChoiceStat;
import com.example.habit.repository.stats.ReviewStatsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewAnswerStatService {
	private final ReviewStatsRepository reviewStatsRepository;

	public List<QuestionStatDto> getAnsweredChoicesGroupedByQuestion(Long habitId) {

		List<AnsweredChoiceStat> stat = reviewStatsRepository.getAnswerdChoicesAndCountGroupByQuestion(habitId);

		Map<Long, QuestionAggregate> grouped = new LinkedHashMap<>();

		for (AnsweredChoiceStat row : stat) {

			//QuestionIdをキーに質問・選択肢情報を集約
			grouped.computeIfAbsent(row.questionId(), id -> new QuestionAggregate(
					id,
					row.questionLabel(),
					new ArrayList<>())).choices.add(
							new ChoiceStatDto(row.choiceId(),
									row.choiceLabel(),
									row.answerCount()));
		}

		return grouped.values().stream()
				.map(qa -> new QuestionStatDto(
						qa.questionId,
						qa.questionLabel,
						qa.choices
						))
				.collect(Collectors.toList());

	}

	//内部ロジック用の中間集計クラス
	private class QuestionAggregate {
		Long questionId;
		String questionLabel;
		List<ChoiceStatDto> choices;

		QuestionAggregate(Long id, String label, List<ChoiceStatDto> choices) {
			this.questionId = id;
			this.questionLabel = label;
			this.choices = choices;
		}
	}

}
