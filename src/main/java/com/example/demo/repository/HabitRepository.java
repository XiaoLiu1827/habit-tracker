package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Habit;
import com.example.demo.model.User;

public interface HabitRepository extends JpaRepository<Habit, Long> {
	// ユーザの習慣をisActive=trueに絞って全取得
	List<Habit> findByUserAndIsActiveTrue(User user);
}