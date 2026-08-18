package com.anuj.algotracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.anuj.algotracker.dto.ProblemRequest;
import com.anuj.algotracker.dto.ProblemResponse;
import com.anuj.algotracker.model.Difficulty;
import com.anuj.algotracker.model.Problem;
import com.anuj.algotracker.model.ProblemStatus;
import com.anuj.algotracker.model.User;
import com.anuj.algotracker.repository.ProblemRepository;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

class ProblemServiceTest {

    @Test
    void createProblemSuccessfully() {

        // Arrange
        ProblemRepository problemRepository = mock(ProblemRepository.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        CurrentUserService currentUserService = mock(CurrentUserService.class);

        ProblemService problemService = new ProblemService(
                problemRepository,
                modelMapper,
                currentUserService
        );

        User user = new User();
        user.setId(1L);
        user.setEmail("anuj@gmail.com");

        ProblemRequest request = new ProblemRequest();
        request.setTitle("Two Sum");
        request.setDescription("Find two numbers that add up to target");
        request.setDifficulty(Difficulty.EASY);
        request.setTopic("Array");
        request.setLink("https://example.com");
        request.setStatus(ProblemStatus.TODO);

        Problem savedProblem = new Problem();
        savedProblem.setTitle("Two Sum");
        savedProblem.setUser(user);

        ProblemResponse response = new ProblemResponse();
        response.setTitle("Two Sum");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(problemRepository.save(any(Problem.class)))
                .thenReturn(savedProblem);

        when(modelMapper.map(savedProblem, ProblemResponse.class))
                .thenReturn(response);

        // Act
        ProblemResponse result = problemService.createProblem(request);

        // Assert
        assertEquals("Two Sum", result.getTitle());

        verify(currentUserService).getCurrentUser();
        verify(problemRepository).save(any(Problem.class));
        verify(modelMapper).map(savedProblem, ProblemResponse.class);
    }

    
}