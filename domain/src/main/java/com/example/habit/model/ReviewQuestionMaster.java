package com.example.habit.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class ReviewQuestionMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String questionLabel;
	 
	// 続けたい or やめたい習慣に適用されるか
	// CONTINUE 用 or QUIT 用
	@Enumerated(EnumType.STRING)
	private HabitType habitType; 

	 // 成功 or 失敗用の質問か
    @Enumerated(EnumType.STRING)
    private ReviewType reviewType;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReviewChoiceMaster> choices;
}
