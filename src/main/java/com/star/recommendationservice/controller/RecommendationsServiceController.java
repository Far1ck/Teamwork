package com.star.recommendationservice.controller;

import com.star.recommendationservice.error.UserNotFoundException;
import com.star.recommendationservice.model.recommendation.UserRecommendations;
import com.star.recommendationservice.service.RecommendationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationsServiceController {

    private final RecommendationsService recommendationsService;

    public RecommendationsServiceController(RecommendationsService recommendationsService) {
        this.recommendationsService = recommendationsService;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFoundHandler(UserNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // Получение всех подходящих рекомендаций для пользователя с определенным ID
    @GetMapping("/{user_id}")
    public ResponseEntity<UserRecommendations> getUserRecommendations (@PathVariable UUID user_id) {
        UserRecommendations recommendations =recommendationsService.getUserRecommendations(user_id);
        return ResponseEntity.ok(recommendations);
    }
}
