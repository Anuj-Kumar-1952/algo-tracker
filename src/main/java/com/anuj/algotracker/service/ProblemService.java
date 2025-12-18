package com.anuj.algotracker.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.anuj.algotracker.dto.ProblemRequest;
import com.anuj.algotracker.dto.ProblemResponse;
import com.anuj.algotracker.model.Difficulty;
import com.anuj.algotracker.model.Problem;
import com.anuj.algotracker.model.ProblemStatus;
import com.anuj.algotracker.repository.ProblemRepository;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final ModelMapper modelMapper;

    public ProblemService(ProblemRepository problemRepository, ModelMapper modelMapper) {
        this.problemRepository = problemRepository;
        this.modelMapper = modelMapper;
    }

    /* ================= CREATE ================= */

    public ProblemResponse createProblem(ProblemRequest request) {
        Problem problem = new Problem();
        problem.setTitle(request.getTitle());
        problem.setDescription(request.getDescription());
        problem.setDifficulty(request.getDifficulty());
        problem.setTopic(request.getTopic());
        problem.setLink(request.getLink());
        problem.setStatus(
                request.getStatus() != null ? request.getStatus() : ProblemStatus.TODO);

        Problem saved = problemRepository.save(problem);
        return modelMapper.map(saved, ProblemResponse.class);
    }

    /* ================= READ ================= */

    public List<ProblemResponse> getAllProblems() {
        return problemRepository.findAll()
                .stream()
                .map(problem -> modelMapper.map(problem, ProblemResponse.class))
                .toList();
    }

    public ProblemResponse getProblemById(Long id) {
        return modelMapper.map(getProblemEntityById(id), ProblemResponse.class);
    }

    /* ================= UPDATE ================= */

    public ProblemResponse updateProblem(Long id, ProblemRequest request) {
        Problem existing = getProblemEntityById(id);

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
        problemRepository.delete(getProblemEntityById(id));
    }

    /* ================= FILTERS ================= */

    public List<ProblemResponse> getProblemsByDifficulty(Difficulty difficulty) {
        return problemRepository.findByDifficulty(difficulty)
                .stream()
                .map(p -> modelMapper.map(p, ProblemResponse.class))
                .toList();
    }

    public List<ProblemResponse> getProblemsByStatus(ProblemStatus status) {
        return problemRepository.findByStatus(status)
                .stream()
                .map(p -> modelMapper.map(p, ProblemResponse.class))
                .toList();
    }

    public List<ProblemResponse> getProblemsByTopic(String topic) {
        return problemRepository.findByTopic(topic)
                .stream()
                .map(p -> modelMapper.map(p, ProblemResponse.class))
                .toList();
    }

    /* ================= INTERNAL ================= */

    private Problem getProblemEntityById(Long id) {
        return problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));
    }
}
