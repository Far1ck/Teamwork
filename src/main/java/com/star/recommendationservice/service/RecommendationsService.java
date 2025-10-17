package com.star.recommendationservice.service;

import com.star.recommendationservice.error.UserNotFoundException;
import com.star.recommendationservice.model.DynamicRule;
import com.star.recommendationservice.model.Recommendation;
import com.star.recommendationservice.model.Rule;
import com.star.recommendationservice.model.UserRecommendations;
import com.star.recommendationservice.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationsService {

    private DynamicRuleRepository dynamicRuleRepository;
    private RuleChecker ruleChecker;

    public RecommendationsService(DynamicRuleRepository dynamicRuleRepository, RuleChecker ruleChecker) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.ruleChecker = ruleChecker;
    }

    public UserRecommendations getUserRecommendations(UUID userId) throws UserNotFoundException {
        ruleChecker.userIsExistCheck(userId);
        List<DynamicRule> ruleList = dynamicRuleRepository.findAll();
        Set<Recommendation> recommendationSet = new LinkedHashSet<>();
        Recommendation recommendation = new Recommendation("", UUID.randomUUID(), "");
        for(DynamicRule dynamicRule : ruleList) {
            int fulfillmentOfConditions = 1;
            for(Rule rule : dynamicRule.getRule()) {
                fulfillmentOfConditions *= ruleChecker.ruleCheck(userId, rule);
                if (fulfillmentOfConditions == 0) break;
            }
            if(fulfillmentOfConditions == 1) {
                recommendation = new Recommendation(dynamicRule.getProduct_name(),
                        UUID.fromString(dynamicRule.getProduct_id()),
                        dynamicRule.getProduct_text());
                recommendationSet.add(recommendation);
            }
        }
        return new UserRecommendations(userId, recommendationSet.stream().toList());
    }
}
