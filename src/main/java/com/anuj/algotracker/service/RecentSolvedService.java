package com.anuj.algotracker.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.anuj.algotracker.datastructure.MyLinkedList;
import com.anuj.algotracker.dto.ProblemResponse;
import com.anuj.algotracker.model.Problem;
import com.anuj.algotracker.model.ProblemStatus;
import com.anuj.algotracker.model.User;
import com.anuj.algotracker.repository.ProblemRepository;

@Service
public class RecentSolvedService {

    private final CurrentUserService currentUserService;
    private final ProblemRepository problemRepository;
    private final ModelMapper modelMapper;

    public RecentSolvedService(CurrentUserService currentUserService,
                               ProblemRepository problemRepository,
                               ModelMapper modelMapper) {
        this.currentUserService = currentUserService;
        this.problemRepository = problemRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Returns recently solved problems (status = DONE)
     * using custom MyLinkedList. Newest solved appear first.
     */
    public List<ProblemResponse> getRecentlySolvedProblems(int limit) {

        if (limit <= 0) {
            return List.of();
        }

        User currentUser = currentUserService.getCurrentUser();

        // 1) Load all problems for this user
        List<Problem> userProblems = problemRepository.findByUser(currentUser);

        // 2) Use your custom MyLinkedList
        MyLinkedList<Problem> solvedList = new MyLinkedList<>();

        // Add DONE problems at the front, so most recent-like first
        for (Problem p : userProblems) {
            if (p.getStatus() == ProblemStatus.DONE) { // change DONE → SOLVED if your enum uses SOLVED
                solvedList.addFirst(p);
            }
        }

        // 3) Move first N from MyLinkedList to normal List
        List<Problem> result = new ArrayList<>();
        int count = Math.min(limit, solvedList.size());

        for (int i = 0; i < count; i++) {
            result.add(solvedList.get(i));
        }

        // 4) Map entities → DTOs
        return result.stream()
                .map(problem -> modelMapper.map(problem, ProblemResponse.class))
                .toList();
    }
}
