package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class ReviewSubmitRequest {
    private Long reviewRecordId;
    private List<Long> choiceIds;
}