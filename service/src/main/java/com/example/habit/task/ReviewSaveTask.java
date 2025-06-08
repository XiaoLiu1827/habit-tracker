package com.example.habit.task;

import java.util.List;

import com.example.habit.model.ReviewAnswer;
import com.example.habit.model.ReviewRecord;
import com.example.habit.model.ReviewStatus;
import com.example.habit.repository.ReviewAnswerRepository;
import com.example.habit.repository.ReviewRecordRepository;

/*振り返りの回答記録のみ非同期で処理するタスク*/
public class ReviewSaveTask implements Runnable {

    private final ReviewRecord record;
    private final List<Long> answerIds;
    private final ReviewRecordRepository reviewRecordRepository;
    private final ReviewAnswerBuilder answerBuilder;
    private final ReviewAnswerRepository reviewAnswerRepository;

    public ReviewSaveTask(ReviewRecord record, List<Long> answerIds,
                          ReviewRecordRepository reviewRecordRepository,
                          ReviewAnswerBuilder answerBuilder,
                          ReviewAnswerRepository reviewAnswerRepository) {
        this.record = record;
        this.answerIds = answerIds;
        this.reviewRecordRepository = reviewRecordRepository;
        this.answerBuilder = answerBuilder;
        this.reviewAnswerRepository = reviewAnswerRepository;
    }

    @Override
    public void run() {
        try {
        	System.out.println(answerIds + "：タスク実行開始");
            List<ReviewAnswer> answers = answerBuilder.buildAnswersFromIds(answerIds, record);
            //カスケードが効かないため明示的に保存
            reviewAnswerRepository.saveAll(answers);
            record.setAnswers(answers);
            record.setStatus(ReviewStatus.SUCCESS);
        } catch (Exception e) {
            record.setStatus(ReviewStatus.FAILED);
            // 実務ではログ出力も重要
            System.err.printf("ReviewRecord保存失敗: habitId=%d, date=%s, error=%s%n",
                    record.getHabit().getId(), record.getDate(), e.getMessage());
        } finally {
            //カスケードが効かないため明示的に保存
        	reviewRecordRepository.save(record);
        }
    }

    /** Answer生成処理を外から注入するためのインターフェース（テストや分離のため
     * =Taskが ReviewAnswer の具体的生成ロジックに依存しない） */
    @FunctionalInterface
    public interface ReviewAnswerBuilder {
        List<ReviewAnswer> buildAnswersFromIds(List<Long> answerIds, ReviewRecord record);
    }
}

