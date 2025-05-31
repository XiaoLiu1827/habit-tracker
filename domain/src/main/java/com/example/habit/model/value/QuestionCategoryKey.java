package com.example.habit.model.value;

import com.example.habit.model.HabitType;
import com.example.habit.model.ReviewType;

/**
 * 振り返り質問の分類キー（習慣タイプ × 成功／失敗）
 * グルーピング処理などに使用される値オブジェクト。
 * 安全にキーを使って比較できるようrecordにする
 * ※メモ：record採用理由
 * ①equals/hashCode/toString の自動実装
 * ②HabitType,ReviewTypeの値は不変のまま使用したい
 * ③データのまとまりであることが明瞭
 */
public record QuestionCategoryKey(HabitType habitType, ReviewType reviewType) {
	 public String toKeyString() {
	        return habitType.name().toUpperCase() + "_" + reviewType.name().toUpperCase();
	    }
}
