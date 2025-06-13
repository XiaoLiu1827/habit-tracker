package com.example.habit.repository.stats;

import java.util.List;

import com.example.habit.model.view.AnsweredChoiceStat;

public interface ReviewStatsRepository {
    List<AnsweredChoiceStat> getAnswerdChoicesAndCountGroupByQuestion(Long habitId);

}
