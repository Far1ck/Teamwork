package com.star.recommendationservice.controller;

import com.star.recommendationservice.model.UserRecommendations;
import com.star.recommendationservice.service.RecommendationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationsServiceController {

    private final RecommendationsService recommendationsService;

    public RecommendationsServiceController(RecommendationsService recommendationsService) {
        this.recommendationsService = recommendationsService;
    }

    @GetMapping("/userId")
    public ResponseEntity<UserRecommendations> getUserRecommendations (UUID userId) {
        UserRecommendations recommendations =recommendationsService.getUserRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }
}
