package com.example.demo.model.value;

import com.example.demo.model.HabitType;
import com.example.demo.model.ReviewType;

/**
 * 振り返り質問の分類キー（習慣タイプ × 成功／失敗）
 * グルーピング処理などに使用される値オブジェクト。
 */
public record QuestionCategoryKey(HabitType habitType, ReviewType reviewType) {
	 public String toKeyString() {
	        return habitType.name().toUpperCase() + "_" + reviewType.name().toUpperCase();
	    }
}
