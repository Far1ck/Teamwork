package com.star.recommendationservice.service.ruleset;

import com.star.recommendationservice.model.Recommendation;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<Recommendation> checkRecommendationRuleSet(UUID userId);
}
