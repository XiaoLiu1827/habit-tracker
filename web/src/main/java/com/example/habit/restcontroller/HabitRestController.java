package com.example.habit.restcontroller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.habit.model.Habit;
import com.example.habit.service.HabitService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/habits")
public class HabitRestController {

    private final HabitService habitService;

    @GetMapping
    public List<Habit> getAllHabits(@RequestParam Long userId) {
        return habitService.getActiveHabitsByUser(userId);
    }
}
