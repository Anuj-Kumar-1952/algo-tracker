package com.anuj.algotracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anuj.algotracker.dto.ProblemRequest;
import com.anuj.algotracker.dto.ProblemResponse;
import com.anuj.algotracker.model.Difficulty;
import com.anuj.algotracker.model.ProblemStatus;
import com.anuj.algotracker.service.ProblemHistoryService;
import com.anuj.algotracker.service.ProblemQueueService;
import com.anuj.algotracker.service.ProblemService;
import com.anuj.algotracker.service.RecentSolvedService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/problems")
@SecurityRequirement(name = "bearerAuth")
public class ProblemController {

    private final ProblemService problemService;
    private final ProblemHistoryService problemHistoryService;
    private final ProblemQueueService problemQueueService;
    private final RecentSolvedService recentSolvedService;

    public ProblemController(ProblemService problemService,
            ProblemHistoryService problemHistoryService,
            ProblemQueueService problemQueueService,
            RecentSolvedService recentSolvedService) {

        this.problemService = problemService;
        this.problemHistoryService = problemHistoryService;
        this.problemQueueService = problemQueueService;
        this.recentSolvedService = recentSolvedService;
    }

    // Create
    @PostMapping
    public ProblemResponse createProblem(@Valid @RequestBody ProblemRequest request) {
        return problemService.createProblem(request);
    }

    @PostMapping("/bulk")
    public List<ProblemResponse> createProblemBulk(
            @Valid @RequestBody List<ProblemRequest> requests) {
        return problemService.createProblemBulk(requests);
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
    
    // Delete all problems of current user
    @DeleteMapping
    public String deleteMyProblems() {
        long deleted = problemService.deleteAllProblemsOfCurrentUser();
        return deleted + " problems deleted successfully";
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

    // Get recent problems in reverse order using custom stack
    // Example: /api/problems/recent/reversed?limit=5
    @GetMapping("/recent/reversed")
    public List<ProblemResponse> getRecentProblemsReversed(
            @RequestParam(defaultValue = "5") int limit) {
        return problemHistoryService.getRecentProblemsReversed(limit);
    }

    // Get next problems from a practice queue (using custom MyQueue)
    // Example: /api/problems/queue/next?limit=5
    @GetMapping("/queue/next")
    public List<ProblemResponse> getNextFromQueue(
            @RequestParam(defaultValue = "5") int limit) {
        return problemQueueService.getNextProblemsFromQueue(limit);
    }

    // Get recently solved problems using custom MyLinkedList
    // Example: /api/problems/solved/recent?limit=5
    @GetMapping("/solved/recent")
    public List<ProblemResponse> getRecentlySolved(
            @RequestParam(defaultValue = "5") int limit) {
        return recentSolvedService.getRecentlySolvedProblems(limit);
    }

}
