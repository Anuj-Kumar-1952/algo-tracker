package com.anuj.algotracker.service;

import com.anuj.algotracker.datastructure.MyStack;
import com.anuj.algotracker.dto.ProblemResponse;
import com.anuj.algotracker.model.Problem;
import com.anuj.algotracker.model.User;
import com.anuj.algotracker.repository.ProblemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemHistoryService {

    private final CurrentUserService currentUserService;
    private final ProblemRepository problemRepository;
    private final ModelMapper modelMapper;

    public ProblemHistoryService(CurrentUserService currentUserService,
            ProblemRepository problemRepository,
            ModelMapper modelMapper) {
        this.currentUserService = currentUserService;
        this.problemRepository = problemRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Returns recent problems in reverse order using a custom stack.
     *
     * @param limit maximum number of recent problems to return
     */
    public List<ProblemResponse> getRecentProblemsReversed(int limit) {

        User currentUser = currentUserService.getCurrentUser();

        // Load all problems for this user
        List<Problem> userProblems = problemRepository.findByUser(currentUser);

        if (userProblems.isEmpty() || limit <= 0) {
            return List.of();
        }

        // If list is big, we only take last 'limit' items
        int total = userProblems.size();
        int count = Math.min(limit, total);

        MyStack<Problem> stack = new MyStack<>();

        // Push last 'count' problems on stack
        // start from the end of the list
        for (int i = total - 1; i >= total - count; i--) {
            stack.push(userProblems.get(i));
        }

        // Pop from stack to get them in reverse order
        List<Problem> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        // Map entities → DTOs
        return result.stream()
                .map(problem -> modelMapper.map(problem, ProblemResponse.class))
                .toList();
    }
}
