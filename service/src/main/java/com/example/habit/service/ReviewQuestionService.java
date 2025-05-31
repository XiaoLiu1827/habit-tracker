package com.example.habit.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.habit.dto.QuestionWithChoicesDto;
import com.example.habit.model.ReviewQuestionMaster;
import com.example.habit.model.value.QuestionCategoryKey;
import com.example.habit.repository.ReviewQuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewQuestionService {

	private final ReviewQuestionRepository questionRepository;

	//習慣タイプと結果区分で分類された質問群を返す
	public Map<String, List<QuestionWithChoicesDto>> getGroupedQuestions() {
		//質問を全件取得
		List<ReviewQuestionMaster> allQuestions = questionRepository.findAll();

		//habitTypeとReviewTypeでグルーピングする
		Map<QuestionCategoryKey, List<QuestionWithChoicesDto>> grouped = allQuestions.stream()
				.collect(Collectors.groupingBy(
						q -> new QuestionCategoryKey(q.getHabitType(), q.getReviewType()),
						Collectors.mapping(QuestionWithChoicesDto::from, Collectors.toList())));

		//キーをStringに変換して、Jsonで扱いやすい形式にする
		return grouped.entrySet().stream() //Mapのkey-valueペア(1エントリ)をSet(エントリの集合)として取り出す
				.collect(Collectors.toMap( //加工したエントリから新たなmapを作成
						entry -> entry.getKey().toKeyString(), // キー：record型 → "CONTINUE_SUCCESS" のような文字列へ変換
						Map.Entry::getValue));// 値：List<QuestionWithChoicesDto> はそのまま使う

	}
}
