package com.example.habit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.habit.model.Habit;
import com.example.habit.model.User;

public interface HabitRepository extends JpaRepository<Habit, Long> {
	// ユーザの習慣をisActive=trueに絞って全取得
	List<Habit> findByUserAndIsActiveTrue(User user);
}