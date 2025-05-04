package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ReviewRecordRequest;
import com.example.demo.model.Habit;
import com.example.demo.model.ReviewAnswer;
import com.example.demo.model.ReviewChoiceMaster;
import com.example.demo.model.ReviewRecord;
import com.example.demo.model.User;
import com.example.demo.repository.HabitRepository;
import com.example.demo.repository.ReviewAnswerRepository;
import com.example.demo.repository.ReviewChoiceRepository;
import com.example.demo.repository.ReviewQuestionRepository;
import com.example.demo.repository.ReviewRecordRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final HabitRepository habitRepository;
	private final ReviewRecordRepository reviewRecordRepository;
	private final ReviewQuestionRepository reviewQuestionRepository;
	private final ReviewChoiceRepository reviewChoiceRepository;
	private final ReviewAnswerRepository reviewAnswerRepository;
	private final UserRepository userRepository;

	/**
	* 📝 振り返りの作成（習慣ごとに or 1日1件）
	*/

	public void saveAllReviewRecords(List<ReviewRecordRequest> requestList) {
		User user = getUser();

		for (ReviewRecordRequest request : requestList) {
			Habit habit = getHabit(request.getHabitId());
			ReviewRecord record = new ReviewRecord();
			record.setHabit(habit);
			record.setUser(user);
			record.setDate(LocalDate.now());
			record.setSuccess(request.isSuccess());

			List<ReviewAnswer> answers = buildAnswersFromIds(request.getAnswerIds(), record);

			record.setAnswers(answers);
			reviewRecordRepository.save(record);
		}
	}

	private List<ReviewAnswer> buildAnswersFromIds(List<Long> ids, ReviewRecord record) {
		return ids.stream().filter(Objects::nonNull)
				.distinct()
				.map(id -> {
					ReviewChoiceMaster choice = reviewChoiceRepository.findById(id)
							.orElseThrow(() -> new IllegalArgumentException("選択肢が見つかりません: " + id));
					return new ReviewAnswer(null, record, choice);
				})
				.toList();
	}

	private User getUser() {
		return userRepository.findById(1L) // TODO: 認証導入時に置換
				.orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません"));
	}

	private Habit getHabit(Long id) {
		return habitRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("該当の習慣が存在しません"));
	}
}
