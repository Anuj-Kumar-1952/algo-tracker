package com.anuj.algotracker.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.anuj.algotracker.datastructure.MyArrayList;
import com.anuj.algotracker.dto.ProblemResponse;
import com.anuj.algotracker.model.Difficulty;
import com.anuj.algotracker.model.Problem;
import com.anuj.algotracker.model.ProblemStatus;
import com.anuj.algotracker.model.User;
import com.anuj.algotracker.repository.ProblemRepository;

@Service
public class RecommendationService {

    private final CurrentUserService currentUserService;
    private final ProblemRepository problemRepository;
    private final ModelMapper modelMapper;

    public RecommendationService(CurrentUserService currentUserService,
            ProblemRepository problemRepository,
            ModelMapper modelMapper) {
        this.currentUserService = currentUserService;
        this.problemRepository = problemRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Recommend next problems for the current user.
     *
     * @param topic      filter by topic (e.g. "ARRAY"), can be null/blank
     * @param difficulty filter by difficulty (EASY/MEDIUM/HARD), can be null
     * @param limit      max number of problems to return
     */
    public List<ProblemResponse> recommendNextProblems(String topic,
            Difficulty difficulty,
            int limit) {

        // 1) Get current logged-in user
        User currentUser = currentUserService.getCurrentUser();

        // 2) Load all problems of this user from DB
        List<Problem> userProblems = problemRepository.findByUser(currentUser);

        // 3) Use our custom MyArrayList to store candidates
        MyArrayList<Problem> candidates = new MyArrayList<>();

        // 4) Simple filtering using if conditions
        for (Problem p : userProblems) {

            boolean matchesTopic = true;
            if (topic != null && !topic.isBlank()) {
                matchesTopic = p.getTopic() != null &&
                        p.getTopic().equalsIgnoreCase(topic);
            }

            boolean matchesDifficulty = true;
            if (difficulty != null) {
                matchesDifficulty = p.getDifficulty() == difficulty;
            }

            // Only recommend if not DONE (or SOLVED, etc.)
            boolean isNotDone = p.getStatus() == null ||
                    p.getStatus() != ProblemStatus.DONE; // adjust enum value if needed

            if (matchesTopic && matchesDifficulty && isNotDone) {
                candidates.add(p);
            }
        }

        // 5) Convert from MyArrayList to normal List, apply limit
        List<Problem> result = new ArrayList<>();
        int count = Math.min(limit, candidates.size());

        for (int i = 0; i < count; i++) {
            result.add(candidates.get(i));
        }

        // 6) Map entities -> DTOs
        return result.stream()
                .map(problem -> modelMapper.map(problem, ProblemResponse.class))
                .toList();
    }
}
