package com.star.recommendationservice.controller;

import com.star.recommendationservice.repository.RecommendationsServiceRepository;
import com.star.recommendationservice.service.ManagementService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagementController.class)
public class ManagementControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BuildProperties buildProperties;

    @MockBean
    private RecommendationsServiceRepository recommendationsServiceRepository;

    @SpyBean
    private ManagementService managementService;

    @InjectMocks
    private ManagementController managementController;

    @Test
    public void getInfoTest() throws Exception {
        String name = "Recommendation Service";
        String version = "0.0.1-SNAPSHOT";

        when(buildProperties.getName()).thenReturn(name);
        when(buildProperties.getVersion()).thenReturn(version);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/management/info")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.version").value(version));
    }
}
