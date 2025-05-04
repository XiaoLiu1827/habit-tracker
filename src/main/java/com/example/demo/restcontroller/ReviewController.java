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
import com.example.demo.dto.ReviewRecordRequest;
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
	 * 振り返り記録の登録
	 */

	@PostMapping("/records")
	public ResponseEntity<Void> saveReviewRecords(@RequestBody List<ReviewRecordRequest> requestList) {
		reviewService.saveAllReviewRecords(requestList);
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
