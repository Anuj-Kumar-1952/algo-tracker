package com.anuj.algotracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anuj.algotracker.dto.ProblemRequest;
import com.anuj.algotracker.dto.ProblemResponse;
import com.anuj.algotracker.model.Difficulty;
import com.anuj.algotracker.model.ProblemStatus;
import com.anuj.algotracker.service.ProblemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    // Create
    @PostMapping
    public ProblemResponse createProblem(@Valid @RequestBody ProblemRequest request) {
        return problemService.createProblem(request);
    }

    // Read all
    @GetMapping
    public List<ProblemResponse> getAllProblems() {
        return problemService.getAllProblems();
    }

    // Read by ID
    @GetMapping("/{id}")
    public ProblemResponse getProblemById(@PathVariable Long id) {
        return problemService.getProblemById(id);
    }

    // Update
    @PutMapping("/{id}")
    public ProblemResponse updateProblem(@PathVariable Long id,
            @Valid @RequestBody ProblemRequest request) {
        return problemService.updateProblem(id, request);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
    }

    // Filter by difficulty
    @GetMapping("/difficulty/{difficulty}")
    public List<ProblemResponse> getByDifficulty(@PathVariable Difficulty difficulty) {
        return problemService.getProblemsByDifficulty(difficulty);
    }

    // Filter by status
    @GetMapping("/status/{status}")
    public List<ProblemResponse> getByStatus(@PathVariable ProblemStatus status) {
        return problemService.getProblemsByStatus(status);
    }

    // Filter by topic
    @GetMapping("/topic/{topic}")
    public List<ProblemResponse> getByTopic(@PathVariable String topic) {
        return problemService.getProblemsByTopic(topic);
    }
}
