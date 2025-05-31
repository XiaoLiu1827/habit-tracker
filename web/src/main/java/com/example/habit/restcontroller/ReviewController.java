package com.example.habit.restcontroller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.habit.dto.QuestionWithChoicesDto;
import com.example.habit.dto.ReviewRecordRequest;
import com.example.habit.dto.ReviewSessionStatusResponse;
import com.example.habit.service.ReviewQuestionService;
import com.example.habit.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	private final ReviewQuestionService reviewQuestionService;

	//振り返り済み判定
	@GetMapping("/session/status")
	public ResponseEntity<ReviewSessionStatusResponse> getReviewStatus(
			@RequestParam("reviewDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reviewDate) {
		ReviewSessionStatusResponse response = reviewService.getTodayReviewStatus(1L, reviewDate);// TODO: 認証導入後に @AuthenticationPrincipal に差し替え
		return ResponseEntity.ok(response);
	}

	/**
	 * 振り返り記録の登録
	 */

	@PostMapping("/records")
	public ResponseEntity<Void> saveReviewRecords(
			@RequestParam("reviewDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reviewDate,
			@RequestBody List<ReviewRecordRequest> requestList) {
		reviewService.saveAllReviewRecords(1L, reviewDate, requestList);// TODO: 認証導入後に @AuthenticationPrincipal に差し替え
		return ResponseEntity.ok().build();
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
}
