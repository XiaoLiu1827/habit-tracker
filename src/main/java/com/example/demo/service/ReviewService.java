package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public ReviewRecord createReview(Long userId, Long habitId, LocalDate date) {
        User user = userRepository.findById(userId).orElseThrow();
        Habit habit = habitRepository.findById(habitId).orElseThrow();

        // 既に同日・同習慣の記録があればスキップ／上書き対応も可能
        Optional<ReviewRecord> existing = reviewRecordRepository.findByUserAndHabitAndDate(user, habit, date);
        if (existing.isPresent()) {
            return existing.get();
        }

        ReviewRecord record = new ReviewRecord();
        record.setUser(user);
        record.setHabit(habit);
        record.setDate(date);

        return reviewRecordRepository.save(record);
    }
    
//    /**
//     * ❓ 習慣タイプに応じた質問を取得
//     */
//    public List<ReviewQuestionMaster> getQuestionsForHabitAndReviewType(HabitType habitType, ReviewType reviewType) {
//        return reviewQuestionRepository.findByHabitTypeAndReviewType(habitType, reviewType);
//    }
    
    /**
     * ✅ 回答の登録（質問IDと選択肢IDのリスト）
     */
    @Transactional
    public void submitAnswers(Long reviewRecordId, List<Long> choiceIds) {
        ReviewRecord record = reviewRecordRepository.findById(reviewRecordId).orElseThrow();
        //習慣ごとに全ての回答を取得
        List<ReviewChoiceMaster> choices = reviewChoiceRepository.findAllById(choiceIds);

        for (ReviewChoiceMaster choice : choices) {
            ReviewAnswer answer = new ReviewAnswer();
            answer.setReviewRecord(record);
            answer.setChoice(choice);
            reviewAnswerRepository.save(answer);
        }
    }
    
    /**
     * 📋 特定の振り返りに対する回答一覧取得
     */
    public List<ReviewAnswer> getAnswersForRecord(Long reviewRecordId) {
        ReviewRecord record = reviewRecordRepository.findById(reviewRecordId).orElseThrow();
        return reviewAnswerRepository.findByReviewRecord(record);
    }

//    /**
//     * 📅 既にその日の振り返りが存在するか確認（1日1件制御など）
//     */
//    public boolean existsReviewForDate(Long userId, LocalDate date) {
//        User user = userRepository.findById(userId).orElseThrow();
//        return reviewRecordRepository.existsByUserAndDate(user, date);
//    }
}
