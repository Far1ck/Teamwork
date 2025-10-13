package com.star.recommendationservice.service;

import com.star.recommendationservice.error.UserNotFoundException;
import com.star.recommendationservice.model.Recommendation;
import com.star.recommendationservice.model.UserRecommendations;
import com.star.recommendationservice.service.ruleset.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationsService {

    private final List<RecommendationRuleSet> rules;

    public RecommendationsService(List<RecommendationRuleSet> rules) {
        this.rules = rules;
    }

    public UserRecommendations getUserRecommendations(UUID userId) throws UserNotFoundException {
        List<Recommendation> recommendations = new ArrayList<>();

        for (RecommendationRuleSet rule : rules) {
            Optional<Recommendation> rec = rule.checkRecommendationRuleSet(userId);
            rec.ifPresent(recommendations::add);
        }
        return new UserRecommendations(userId, recommendations);
    }
}
