package com.star.recommendationservice.rule;

import com.star.recommendationservice.model.Recommendation;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<Recommendation> checkRecommendationRuleSet(UUID userId);
}
