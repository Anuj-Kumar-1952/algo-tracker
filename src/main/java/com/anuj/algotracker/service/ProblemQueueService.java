package com.anuj.algotracker.service;

import com.anuj.algotracker.datastructure.MyQueue;
import com.anuj.algotracker.dto.ProblemResponse;
import com.anuj.algotracker.model.Problem;
import com.anuj.algotracker.model.ProblemStatus;
import com.anuj.algotracker.model.User;
import com.anuj.algotracker.repository.ProblemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemQueueService {

    private final CurrentUserService currentUserService;
    private final ProblemRepository problemRepository;
    private final ModelMapper modelMapper;

    public ProblemQueueService(CurrentUserService currentUserService,
            ProblemRepository problemRepository,
            ModelMapper modelMapper) {
        this.currentUserService = currentUserService;
        this.problemRepository = problemRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Returns next problems to practice using a custom queue (FIFO).
     *
     * @param limit maximum number of problems to return
     */
    public List<ProblemResponse> getNextProblemsFromQueue(int limit) {

        if (limit <= 0) {
            return List.of();
        }

        User currentUser = currentUserService.getCurrentUser();

        // 1) Load all problems of this user
        List<Problem> userProblems = problemRepository.findByUser(currentUser);

        // 2) Build a practice queue: only TODO or IN_PROGRESS
        MyQueue<Problem> queue = new MyQueue<>();

        for (Problem p : userProblems) {
            ProblemStatus status = p.getStatus();
            if (status == null ||
                    status == ProblemStatus.TODO ||
                    status == ProblemStatus.IN_PROGRESS) {
                queue.enqueue(p);
            }
        }

        // 3) Dequeue up to 'limit' problems
        List<Problem> result = new ArrayList<>();
        int count = Math.min(limit, queue.size());

        for (int i = 0; i < count; i++) {
            result.add(queue.dequeue());
        }

        // 4) Map entities -> DTOs
        return result.stream()
                .map(problem -> modelMapper.map(problem, ProblemResponse.class))
                .toList();
    }
}
