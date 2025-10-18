package com.star.recommendationservice.service;

import com.star.recommendationservice.model.DynamicRule;
import com.star.recommendationservice.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Сервис для добавления, получения и удаления динамических правил
@Service
public class RuleService {
    private final DynamicRuleRepository dynamicRuleRepository;

    public RuleService(DynamicRuleRepository dynamicRuleRepository) {
        this.dynamicRuleRepository = dynamicRuleRepository;
    }

    // Получение из базы всех динамических правил
    public List<DynamicRule> getAllRules() {
        return dynamicRuleRepository.findAll();
    }

    // Добавление в базу динамического правила
    public DynamicRule createDynamicRule(DynamicRule dynamicRule) {
        return dynamicRuleRepository.save(dynamicRule);
    }

    // Удаление из базы динамического правила
    public void deleteDynamicRules(String productId) {
        dynamicRuleRepository.deleteByProductId(productId);
    }
}
