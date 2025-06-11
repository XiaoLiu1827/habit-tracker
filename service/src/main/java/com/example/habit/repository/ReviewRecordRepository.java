package com.example.habit.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.habit.model.Habit;
import com.example.habit.model.ReviewRecord;
import com.example.habit.model.User;
import com.example.habit.model.view.StreakStat;
import com.example.habit.model.view.SuccessRateStat;

//開発用（H2)
public interface ReviewRecordRepository extends JpaRepository<ReviewRecord, Long> {
	List<ReviewRecord> findByHabitId(Long habitId);

	//整合性確保のためエンティティ単位で受け取る
	Optional<ReviewRecord> findByUserAndHabitAndDate(User user, Habit habit, LocalDate date);

	@Query(value = """
			SELECT
			  r.habit_id AS habitId,
			  COUNT(*) AS totalCount,
			  SUM(CASE WHEN r.success = true THEN 1 ELSE 0 END) AS successCount
			FROM review_record r
			WHERE r.habit_id = :habitId
			  AND r.date BETWEEN :startDate AND :endDate
			GROUP BY r.habit_id
			""", nativeQuery = true)
	SuccessRateStat getSuccessRateByHabitIdAndDateRange(
			@Param("habitId") Long habitId,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query(value = """
			WITH success_days AS (
			SELECT
			 DATEADD('DAY', ROW_NUMBER() OVER (ORDER BY date DESC) - 1, date)
			 AS group_key,
			 date
			FROM review_record
			WHERE habit_id = :habitId
			AND success = TRUE
			),
			latest_record AS (
			SELECT
		      success
			FROM review_record
			WHERE habit_id = :habitId
			ORDER BY date DESC
			LIMIT 1
			),
			latest_group AS (
			  SELECT group_key
			  FROM success_days
			  ORDER BY date DESC
			  FETCH FIRST 1 ROWS ONLY
			)
			SELECT 
			  CASE
			    WHEN latest_record.success = FALSE THEN 0
			    ELSE (
			      SELECT COUNT(*)
				  FROM success_days
				  WHERE group_key = (SELECT group_key FROM latest_group)
		        )
		      END AS currnt_streak
			FROM latest_record;
			""", nativeQuery = true)
	StreakStat getStreakByHabitId(
			@Param("habitId") Long habitId);

}
