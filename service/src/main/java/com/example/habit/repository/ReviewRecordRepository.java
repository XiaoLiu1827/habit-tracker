package com.example.habit.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.habit.dto.SuccessRateStat;
import com.example.habit.model.Habit;
import com.example.habit.model.ReviewRecord;
import com.example.habit.model.User;

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
}
