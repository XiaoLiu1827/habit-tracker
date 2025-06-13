package com.example.habit.service;

import org.springframework.stereotype.Service;

import com.example.habit.dto.StreakDto;
import com.example.habit.model.view.StreakStat;
import com.example.habit.repository.ReviewRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitStreakService {

	private final ReviewRecordRepository reviewRecordRepository;
	
	public StreakDto getStreak(Long habitId) {

		StreakStat stat = reviewRecordRepository.getStreakByHabitId(habitId);
		int streakDays = (stat != null) ? stat.streakDays().intValue() : 0;
		return new StreakDto(streakDays);

	}

}
