package com.star.recommendationservice.service;

import com.star.recommendationservice.error.UserNotFoundException;
import com.star.recommendationservice.model.DynamicRule;
import com.star.recommendationservice.model.Recommendation;
import com.star.recommendationservice.model.Rule;
import com.star.recommendationservice.model.UserRecommendations;
import com.star.recommendationservice.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.*;

// Сервис для получения всех подходящих рекомендаций для пользователя с заданным ID
@Service
public class RecommendationsService {

    private final DynamicRuleRepository dynamicRuleRepository;
    private final RuleChecker ruleChecker;

    public RecommendationsService(DynamicRuleRepository dynamicRuleRepository, RuleChecker ruleChecker) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.ruleChecker = ruleChecker;
    }

    // Проверка и получение всех динамических правил для пользователя с заданным ID
    // Возвращает обьект UserRecommendations
    public UserRecommendations getUserRecommendations(UUID userId) throws UserNotFoundException {
        // Проверка наличия пользователя
        ruleChecker.userIsExistCheck(userId);
        List<DynamicRule> ruleList = dynamicRuleRepository.findAll(); // Список всех дин. правил из БД
        Set<Recommendation> recommendationSet = new LinkedHashSet<>(); // Набор правил, прошедших проверку
        Recommendation recommendation;
        // Проверка всех динамических правил и в случае успешной проверки добавление их в Set
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
