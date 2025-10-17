package com.star.recommendationservice.service;

import com.star.recommendationservice.model.DynamicRule;
import com.star.recommendationservice.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleService {
    private DynamicRuleRepository dynamicRuleRepository;

    public RuleService(DynamicRuleRepository dynamicRuleRepository) {
        this.dynamicRuleRepository = dynamicRuleRepository;
    }
    public List<DynamicRule> getAllRules() {
        return dynamicRuleRepository.findAll();
    }

    public DynamicRule createDynamicRule(DynamicRule dynamicRule) {
        return dynamicRuleRepository.save(dynamicRule);
    }


    public void deleteDynamicRules(String productId) {
        dynamicRuleRepository.deleteByProductId(productId);
    }
}
