package com.anuj.algotracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuj.algotracker.model.Difficulty;
import com.anuj.algotracker.model.Problem;
import com.anuj.algotracker.model.ProblemStatus;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByDifficulty(Difficulty difficulty);

    List<Problem> findByStatus(ProblemStatus status);

    List<Problem> findByTopic(String topic);

}
