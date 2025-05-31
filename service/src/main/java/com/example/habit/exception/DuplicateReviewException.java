package com.example.habit.exception;

public class DuplicateReviewException extends RuntimeException {
	public DuplicateReviewException(String message) {
		super(message);
	}
}
