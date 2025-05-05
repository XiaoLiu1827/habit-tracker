package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ReviewRecordRequest {
	private Long habitId;
	private boolean success;
	private List<Long> answerIds;
    private LocalDate reviewDate;
}
