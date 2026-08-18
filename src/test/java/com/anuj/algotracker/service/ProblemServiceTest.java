package com.anuj.algotracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
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
                                currentUserService);

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

        @Test
        void userCannotAccessAnotherUsersProblem() {

                // Arrange
                ProblemRepository problemRepository = mock(ProblemRepository.class);
                ModelMapper modelMapper = mock(ModelMapper.class);
                CurrentUserService currentUserService = mock(CurrentUserService.class);

                ProblemService problemService = new ProblemService(
                                problemRepository,
                                modelMapper,
                                currentUserService);

                // Logged-in user
                User currentUser = new User();
                currentUser.setId(1L);
                currentUser.setEmail("userA@gmail.com");

                // Problem belongs to another user
                User problemOwner = new User();
                problemOwner.setId(2L);
                problemOwner.setEmail("userB@gmail.com");

                Problem problem = new Problem();
                problem.setId(10L);
                problem.setTitle("Two Sum");
                problem.setUser(problemOwner);

                when(currentUserService.getCurrentUser())
                                .thenReturn(currentUser);

                when(problemRepository.findById(10L))
                                .thenReturn(java.util.Optional.of(problem));

                // Act + Assert
                RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                                RuntimeException.class,
                                () -> problemService.getProblemById(10L));

                assertEquals(
                                "Problem not found or access denied",
                                exception.getMessage());

                // Problem should never be converted to response
                verify(modelMapper, never())
                                .map(any(Problem.class), eq(ProblemResponse.class));
        }

        @Test
        void updateProblemSuccessfully() {

                // Arrange
                ProblemRepository problemRepository = mock(ProblemRepository.class);
                ModelMapper modelMapper = mock(ModelMapper.class);
                CurrentUserService currentUserService = mock(CurrentUserService.class);

                ProblemService problemService = new ProblemService(
                                problemRepository,
                                modelMapper,
                                currentUserService);

                User user = new User();
                user.setId(1L);
                user.setEmail("anuj@gmail.com");

                Problem problem = new Problem();
                problem.setId(10L);
                problem.setTitle("Old Title");
                problem.setUser(user);

                ProblemRequest request = new ProblemRequest();
                request.setTitle("Updated Title");
                request.setDescription("Updated description");
                request.setDifficulty(Difficulty.MEDIUM);
                request.setTopic("Array");
                request.setLink("https://example.com");
                request.setStatus(ProblemStatus.DONE);

                ProblemResponse response = new ProblemResponse();
                response.setTitle("Updated Title");

                when(currentUserService.getCurrentUser())
                                .thenReturn(user);

                when(problemRepository.findById(10L))
                                .thenReturn(java.util.Optional.of(problem));

                when(problemRepository.save(problem))
                                .thenReturn(problem);

                when(modelMapper.map(problem, ProblemResponse.class))
                                .thenReturn(response);

                // Act
                ProblemResponse result = problemService.updateProblem(10L, request);

                // Assert
                // Assert
                assertEquals("Updated Title", result.getTitle());
                assertEquals("Updated Title", problem.getTitle());
                assertEquals(ProblemStatus.DONE, problem.getStatus());

                verify(currentUserService).getCurrentUser();
                verify(problemRepository).findById(10L);
                verify(problemRepository).save(problem);
                verify(modelMapper).map(problem, ProblemResponse.class);
        }

}