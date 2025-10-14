package com.star.recommendationservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class RecommendationsServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetUserRecommendations() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(get("/recommendation/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(userId.toString()))
                .andExpect(jsonPath("$.recommendations").isArray());
    }
}