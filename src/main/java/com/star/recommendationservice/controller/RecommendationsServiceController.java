package com.star.recommendationservice.controller;

import com.star.recommendationservice.model.UserRecommendations;
import com.star.recommendationservice.service.RecommendationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{user_id}")
    public ResponseEntity<UserRecommendations> getUserRecommendations (@PathVariable UUID user_id) {
        UserRecommendations recommendations =recommendationsService.getUserRecommendations(user_id);
        return ResponseEntity.ok(recommendations);
    }
}
