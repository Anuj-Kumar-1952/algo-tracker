package com.anuj.algotracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anuj.algotracker.dto.DashboardSummary;
import com.anuj.algotracker.model.Difficulty;
import com.anuj.algotracker.model.Problem;
import com.anuj.algotracker.model.ProblemStatus;
import com.anuj.algotracker.model.User;
import com.anuj.algotracker.repository.ProblemRepository;

@Service
public class DashboardService {

    private final CurrentUserService currentUserService;
    private final ProblemRepository problemRepository;

    public DashboardService(CurrentUserService currentUserService,
            ProblemRepository problemRepository) {
        this.currentUserService = currentUserService;
        this.problemRepository = problemRepository;
    }

    public DashboardSummary getSummaryForCurrentUser() {

        User currentUser = currentUserService.getCurrentUser();
        List<Problem> problems = problemRepository.findByUser(currentUser);

        DashboardSummary summary = new DashboardSummary();
        summary.setTotalProblems(problems.size());

        // Using simple arrays as counters

        // index 0 -> EASY, 1 -> MEDIUM, 2 -> HARD
        int[] difficultyCounts = new int[3];

        // index 0 -> TODO, 1 -> IN_PROGRESS, 2 -> DONE
        int[] statusCounts = new int[3];

        for (Problem p : problems) {
            // Count by difficulty
            Difficulty diff = p.getDifficulty();
            if (diff == Difficulty.EASY) {
                difficultyCounts[0]++;
            } else if (diff == Difficulty.MEDIUM) {
                difficultyCounts[1]++;
            } else if (diff == Difficulty.HARD) {
                difficultyCounts[2]++;
            }

            // Count by status
            ProblemStatus status = p.getStatus();
            if (status == ProblemStatus.TODO) {
                statusCounts[0]++;
            } else if (status == ProblemStatus.IN_PROGRESS) {
                statusCounts[1]++;
            } else if (status == ProblemStatus.DONE) {
                statusCounts[2]++;
            }
        }

        summary.setEasyCount(difficultyCounts[0]);
        summary.setMediumCount(difficultyCounts[1]);
        summary.setHardCount(difficultyCounts[2]);

        summary.setTodoCount(statusCounts[0]);
        summary.setInProgressCount(statusCounts[1]);
        summary.setDoneCount(statusCounts[2]);

        return summary;
    }
}
