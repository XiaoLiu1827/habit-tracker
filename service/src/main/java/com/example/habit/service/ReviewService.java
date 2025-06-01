package com.example.habit.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

import org.springframework.stereotype.Service;

import com.example.habit.dto.ReviewRecordRequest;
import com.example.habit.dto.ReviewSessionStatusResponse;
import com.example.habit.exception.DuplicateReviewException;
import com.example.habit.model.Habit;
import com.example.habit.model.ReviewAnswer;
import com.example.habit.model.ReviewChoiceMaster;
import com.example.habit.model.ReviewRecord;
import com.example.habit.model.ReviewSession;
import com.example.habit.model.User;
import com.example.habit.repository.HabitRepository;
import com.example.habit.repository.ReviewAnswerRepository;
import com.example.habit.repository.ReviewChoiceRepository;
import com.example.habit.repository.ReviewQuestionRepository;
import com.example.habit.repository.ReviewRecordRepository;
import com.example.habit.repository.ReviewSessionRepository;
import com.example.habit.repository.UserRepository;

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
	private final ReviewSessionRepository reviewSessionRepository;
	private final ExecutorService executor;

	//振り返り実施済み判定
	public ReviewSessionStatusResponse getTodayReviewStatus(Long userId, LocalDate reviewDate) {
		// 対象の日時で振り返りを実施済みか判定
		boolean reviewed = reviewSessionRepository.existsByUserIdAndReviewDate(userId, reviewDate);

		return new ReviewSessionStatusResponse(reviewed);
	}

	/**
	* 📝 振り返りの作成（習慣ごとに or 1日1件）
	*/

	public void saveAllReviewRecords(Long userId, LocalDate reviewDate, List<ReviewRecordRequest> requestList) {
		User user = getUser(userId);

		// 既にセッションが存在するか確認（＝すでに振り返り済みか）
		if (reviewSessionRepository.existsByUserIdAndReviewDate(userId, reviewDate)) {
			throw new DuplicateReviewException("この日はすでに振り返り済みです。");
		}

		// セッションを作成
		ReviewSession session = new ReviewSession(userId, reviewDate, LocalDateTime.now());
		reviewSessionRepository.save(session);

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

	private User getUser(Long userId) {
		return userRepository.findById(userId) // TODO: 認証導入時に置換
				.orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません"));
	}

	private Habit getHabit(Long id) {
		return habitRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("該当の習慣が存在しません"));
	}
}
