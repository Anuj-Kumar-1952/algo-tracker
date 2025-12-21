package com.anuj.algotracker.dto;

import com.anuj.algotracker.model.Difficulty;
import com.anuj.algotracker.model.ProblemStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProblemRequest {

    @NotBlank(message = "Title must not be empty")
    private String title;

    private String description;

    @NotNull(message = "Difficulty is required")
    private Difficulty difficulty;

    @NotBlank(message = "Topic must not be empty")
    private String topic;

    private String link;
    
    @NotNull(message = "Status is required")
    private ProblemStatus status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
