package com.example.habit.repository.stats;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.habit.model.view.AnsweredChoiceStat;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewStatsRepositoryImpl implements ReviewStatsRepository {

	private final JdbcTemplate jdbcTemplate;

	@Override
	public List<AnsweredChoiceStat> getAnswerdChoicesAndCountGroupByQuestion(Long habitId) {
		String sql = """
						SELECT
				  rc.id AS choiceId,
				  rc.choice_label AS choiceLabel,
				  rc.question_id AS questionId,
				  rq.question_label AS questionLabel,
				  COUNT(ra.id) AS answerCount
				FROM review_choice_master rc
				LEFT JOIN review_answer ra ON ra.choice_id = rc.id
				 AND ra.review_record_id IN (
				     SELECT id FROM review_record WHERE habit_id = ?
				 )
				INNER JOIN review_question_master rq
				ON rq.id = rc.question_id
				INNER JOIN habit h
				ON h.type = rq.habit_type
				WHERE h.id = ?
				GROUP BY rc.id, rc.choice_label, rc.question_id, rq.question_label
				ORDER BY rc.question_id, rc.id
				""";

		return jdbcTemplate.query(sql, new Object[] { habitId, habitId }, (rs, rowNum) -> new AnsweredChoiceStat(
				rs.getLong("choiceId"),
				rs.getString("choiceLabel"),
				rs.getLong("questionId"),
				rs.getString("questionLabel"),
				rs.getLong("answerCount")));
	}
}