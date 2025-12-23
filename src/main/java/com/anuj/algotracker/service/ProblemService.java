package com.anuj.algotracker.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.anuj.algotracker.dto.ProblemRequest;
import com.anuj.algotracker.dto.ProblemResponse;
import com.anuj.algotracker.model.Difficulty;
import com.anuj.algotracker.model.Problem;
import com.anuj.algotracker.model.ProblemStatus;
import com.anuj.algotracker.model.User;
import com.anuj.algotracker.repository.ProblemRepository;

import jakarta.transaction.Transactional;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final ModelMapper modelMapper;
    private final CurrentUserService currentUserService;

    public ProblemService(
            ProblemRepository problemRepository,
            ModelMapper modelMapper,
            CurrentUserService currentUserService) {

        this.problemRepository = problemRepository;
        this.modelMapper = modelMapper;
        this.currentUserService = currentUserService;
    }

    /* ================= CREATE ================= */

    public ProblemResponse createProblem(ProblemRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        Problem problem = new Problem();
        problem.setTitle(request.getTitle());
        problem.setDescription(request.getDescription());
        problem.setDifficulty(request.getDifficulty());
        problem.setTopic(request.getTopic());
        problem.setLink(request.getLink());
        problem.setStatus(
                request.getStatus() != null ? request.getStatus() : ProblemStatus.TODO);

        problem.setUser(currentUser);

        return modelMapper.map(problemRepository.save(problem), ProblemResponse.class);
    }

    /* ================= BULK CREATE ================= */

    public List<ProblemResponse> createProblemBulk(List<ProblemRequest> requests) {

        User currentUser = currentUserService.getCurrentUser();

        List<Problem> problems = requests.stream()
                .map(request -> {
                    Problem problem = new Problem();
                    problem.setTitle(request.getTitle());
                    problem.setDescription(request.getDescription());
                    problem.setDifficulty(request.getDifficulty());
                    problem.setTopic(request.getTopic());
                    problem.setLink(request.getLink());
                    problem.setStatus(
                            request.getStatus() != null
                                    ? request.getStatus()
                                    : ProblemStatus.TODO);

                    problem.setUser(currentUser);
                    return problem;
                })
                .toList();

        return problemRepository.saveAll(problems)
                .stream()
                .map(problem -> modelMapper.map(problem, ProblemResponse.class))
                .toList();
    }

    /* ================= READ ================= */

    public List<ProblemResponse> getAllProblems() {
        User currentUser = currentUserService.getCurrentUser();

        return problemRepository.findByUser(currentUser)
                .stream()
                .map(problem -> modelMapper.map(problem, ProblemResponse.class))
                .toList();
    }

    public ProblemResponse getProblemById(Long id) {
        return modelMapper.map(getOwnedProblem(id), ProblemResponse.class);
    }

    /* ================= UPDATE ================= */

    public ProblemResponse updateProblem(Long id, ProblemRequest request) {
        Problem existing = getOwnedProblem(id);

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setDifficulty(request.getDifficulty());
        existing.setTopic(request.getTopic());
        existing.setLink(request.getLink());

        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }

        return modelMapper.map(problemRepository.save(existing), ProblemResponse.class);
    }

    /* ================= DELETE ================= */
    
    public void deleteProblem(Long id) {
        problemRepository.delete(getOwnedProblem(id));
    }
    
    /* ================= DELETE ================= */

    @Transactional
    public long deleteAllProblemsOfCurrentUser() {
        User currentUser = currentUserService.getCurrentUser();

        long count = problemRepository.countByUser(currentUser);
        problemRepository.deleteByUser(currentUser);

        return count;
    }

    /* ================= FILTERS ================= */

    public List<ProblemResponse> getProblemsByDifficulty(Difficulty difficulty) {
        User currentUser = currentUserService.getCurrentUser();

        return problemRepository.findByUserAndDifficulty(currentUser, difficulty)
                .stream()
                .map(p -> modelMapper.map(p, ProblemResponse.class))
                .toList();
    }

    public List<ProblemResponse> getProblemsByStatus(ProblemStatus status) {
        User currentUser = currentUserService.getCurrentUser();

        return problemRepository.findByUserAndStatus(currentUser, status)
                .stream()
                .map(p -> modelMapper.map(p, ProblemResponse.class))
                .toList();
    }

    public List<ProblemResponse> getProblemsByTopic(String topic) {
        User currentUser = currentUserService.getCurrentUser();

        return problemRepository.findByUserAndTopic(currentUser, topic)
                .stream()
                .map(p -> modelMapper.map(p, ProblemResponse.class))
                .toList();
    }

    /* ================= INTERNAL (AUTHORIZATION) ================= */

    private Problem getOwnedProblem(Long id) {
        User currentUser = currentUserService.getCurrentUser();

        return problemRepository.findById(id)
                .filter(problem -> problem.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new RuntimeException("Problem not found or access denied"));
    }
}
