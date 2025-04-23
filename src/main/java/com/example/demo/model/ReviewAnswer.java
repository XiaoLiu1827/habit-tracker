package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ReviewAnswer {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private ReviewRecord reviewRecord;

	@ManyToOne
	private ReviewChoiceMaster choice;
}
