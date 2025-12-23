package com.anuj.algotracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anuj.algotracker.model.Difficulty;
import com.anuj.algotracker.model.Problem;
import com.anuj.algotracker.model.ProblemStatus;
import com.anuj.algotracker.model.User;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByUser(User user);

    List<Problem> findByUserAndDifficulty(User user, Difficulty difficulty);

    List<Problem> findByUserAndStatus(User user, ProblemStatus status);

    List<Problem> findByUserAndTopic(User user, String topic);

    long countByUser(User user); // For counting problems of a user

    void deleteByUser(User user);

}
