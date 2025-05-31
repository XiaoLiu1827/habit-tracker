package com.example.habit.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReviewStartRequest {
    private Long userId;
    private Long habitId;
    private LocalDate date;
}