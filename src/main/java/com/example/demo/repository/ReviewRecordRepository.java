package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Habit;
import com.example.demo.model.ReviewRecord;
import com.example.demo.model.User;

public interface ReviewRecordRepository extends JpaRepository<ReviewRecord, Long> {
    List<ReviewRecord> findByHabitId(Long habitId);
    //整合性確保のためエンティティ単位で受け取る
    Optional<ReviewRecord> findByUserAndHabitAndDate(User user, Habit habit, LocalDate date);
}
