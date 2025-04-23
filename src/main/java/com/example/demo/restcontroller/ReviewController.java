package com.example.demo.restcontroller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.QuestionWithChoicesDto;
import com.example.demo.dto.ReviewStartRequest;
import com.example.demo.dto.ReviewSubmitRequest;
import com.example.demo.model.ReviewRecord;
import com.example.demo.service.ReviewQuestionService;
import com.example.demo.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	private final ReviewQuestionService reviewQuestionService;

	/**
	 * 振り返りレコードの作成（同じ日・習慣が既にあればそれを返す）
	 */
	@PostMapping("/start")
	public ResponseEntity<ReviewRecord> startReview(@RequestBody ReviewStartRequest request) {
		ReviewRecord record = reviewService.createReview(request.getUserId(), request.getHabitId(), request.getDate());
		return ResponseEntity.ok(record);
	}

	//    /**
	//     * 習慣タイプと振り返り種別に応じた質問を取得
	//     */

	@GetMapping("/questions")
	public Map<String, List<QuestionWithChoicesDto>> getAllGroupedQuestions() {
		return reviewQuestionService.getGroupedQuestions();
	}
	//    @GetMapping("/questions")
	//    public ResponseEntity<List<ReviewQuestionMaster>> getQuestions(
	//            @RequestParam HabitType habitType,
	//            @RequestParam ReviewType reviewType) {
	//        List<ReviewQuestionMaster> questions = reviewService.getQuestionsForHabitAndReviewType(habitType, reviewType);
	//        return ResponseEntity.ok(questions);
	//    }

	/**
	 * 回答の登録（選択肢IDのリスト）
	 */
	@PostMapping("/submit")
	public ResponseEntity<Void> submitAnswers(@RequestBody ReviewSubmitRequest request) {
		reviewService.submitAnswers(request.getReviewRecordId(), request.getChoiceIds());
		return ResponseEntity.ok().build();
	}
}
