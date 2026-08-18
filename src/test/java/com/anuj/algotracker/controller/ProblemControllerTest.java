package com.anuj.algotracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.anuj.algotracker.dto.ProblemRequest;
import com.anuj.algotracker.dto.ProblemResponse;
import com.anuj.algotracker.model.Difficulty;
import com.anuj.algotracker.model.ProblemStatus;
import com.anuj.algotracker.security.CustomUserDetailsService;
import com.anuj.algotracker.security.JWTService;
import com.anuj.algotracker.security.SecurityConfig;
import com.anuj.algotracker.service.ProblemHistoryService;
import com.anuj.algotracker.service.ProblemQueueService;
import com.anuj.algotracker.service.ProblemService;
import com.anuj.algotracker.service.RecentSolvedService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(ProblemController.class)
@Import(SecurityConfig.class)
class ProblemControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        // Security dependencies
        @MockBean
        private JWTService jwtService;

        @MockBean
        private CustomUserDetailsService customUserDetailsService;

        // Controller dependencies
        @MockBean
        private ProblemService problemService;

        @MockBean
        private ProblemHistoryService problemHistoryService;

        @MockBean
        private ProblemQueueService problemQueueService;

        @MockBean
        private RecentSolvedService recentSolvedService;

        @Test
        void createProblemWithInvalidData() throws Exception {

                // Arrange
                ProblemRequest request = new ProblemRequest();

                request.setTitle("");
                request.setDifficulty(null);
                request.setTopic("");
                request.setStatus(null);

                // Act + Assert
                mockMvc.perform(
                                post("/api/problems")
                                                .with(user("anuj@gmail.com"))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error")
                                                .value("Validation Failed"))
                                .andExpect(jsonPath("$.details.title")
                                                .value("Title must not be empty"))
                                .andExpect(jsonPath("$.details.difficulty")
                                                .value("Difficulty is required"))
                                .andExpect(jsonPath("$.details.topic")
                                                .value("Topic must not be empty"))
                                .andExpect(jsonPath("$.details.status")
                                                .value("Status is required"));

                // Service should NOT be called
                verify(problemService, never())
                                .createProblem(any(ProblemRequest.class));
        }

        @Test
        void createProblemSuccessfully() throws Exception {

                // Arrange
                ProblemRequest request = new ProblemRequest();

                request.setTitle("Two Sum");
                request.setDescription("Find two numbers");
                request.setDifficulty(Difficulty.EASY);
                request.setTopic("Array");
                request.setLink("https://example.com");
                request.setStatus(ProblemStatus.TODO);

                ProblemResponse response = new ProblemResponse();
                response.setTitle("Two Sum");

                when(problemService.createProblem(any(ProblemRequest.class)))
                                .thenReturn(response);

                // Act + Assert
                mockMvc.perform(
                                post("/api/problems")
                                                .with(user("anuj@gmail.com"))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Two Sum"));

                // Service should be called once
                verify(problemService).createProblem(any(ProblemRequest.class));
        }
}