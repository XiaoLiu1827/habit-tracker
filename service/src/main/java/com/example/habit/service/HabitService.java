package com.example.habit.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.habit.model.Habit;
import com.example.habit.model.User;
import com.example.habit.repository.HabitRepository;
import com.example.habit.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitService {

	private final HabitRepository habitRepository;
	private final UserRepository userRepository;

	public List<Habit> getActiveHabitsByUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return habitRepository.findByUserAndIsActiveTrue(user);
    }
}
