package com.anuj.algotracker.dto;


import com.anuj.algotracker.model.Difficulty;
import com.anuj.algotracker.model.ProblemStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProblemResponse {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Difficulty difficulty;

    @NotBlank
    private String topic;

    private String link;

    private ProblemStatus status;
}
