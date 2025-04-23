package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Habit;
import com.example.demo.model.User;
import com.example.demo.repository.HabitRepository;
import com.example.demo.repository.UserRepository;

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
