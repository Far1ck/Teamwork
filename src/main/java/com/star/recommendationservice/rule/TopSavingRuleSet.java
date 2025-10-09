package com.star.recommendationservice.rule;

import com.star.recommendationservice.model.Recommendation;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet{

    @Override
    public Optional<Recommendation> checkRecommendationRuleSet(UUID userId) {

        //проверка по условиям, если подходят возвращаем Optional.of(Recommendation...)

        return Optional.empty();
    }
}
