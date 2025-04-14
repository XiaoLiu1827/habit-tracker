package com.example.demo.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Habit;
import com.example.demo.model.HabitRecord;
@Repository
public class HabitRepository {
	private final Map<Long, Habit> habits = new LinkedHashMap<>();
	private final Map<Long, List<HabitRecord>> records = new HashMap<>();
	private final AtomicLong idGenerator = new AtomicLong();

	public List<Habit> findAll() {
		return new ArrayList<>(habits.values());
	}

	
	public Habit save(String name) {
		Long id = idGenerator.incrementAndGet();
		Habit habit = new Habit(id, name);
		habits.put(id, habit);
		records.put(id, new ArrayList<>());
		return habit;
	}

	public void addRecord(Long habitId, boolean success) {
		Long id = idGenerator.incrementAndGet();
		HabitRecord record = new HabitRecord(id,habitId, LocalDate.now(), success);
		records.get(habitId).add(record);
	}

	public List<HabitRecord> getRecords(Long habitId) {
		return records.getOrDefault(habitId, Collections.emptyList());
	}
}
