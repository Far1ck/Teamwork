package com.star.recommendationservice.controller;

import com.star.recommendationservice.model.DynamicRule;
import com.star.recommendationservice.model.DynamicRuleRequest;
import com.star.recommendationservice.model.DynamicRulesList;
import com.star.recommendationservice.model.Rule;
import com.star.recommendationservice.service.RuleService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
public class RuleController {
    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    // Получение всех динамических правил
    @GetMapping
    public ResponseEntity<DynamicRulesList> getAllRules() {
        DynamicRulesList listing = new DynamicRulesList(ruleService.getAllRules());
        return ResponseEntity.ok(listing);
    }

    // Добавление динамического правила
    @PostMapping
    public ResponseEntity<DynamicRule> createDynamicRule(@RequestBody DynamicRuleRequest dynamicRuleRequest) {
        // Создание объекта DynamicRule и его инициализация
        DynamicRule dynamicRule = new DynamicRule();
        dynamicRule.setProduct_name(dynamicRuleRequest.getProductName());
        dynamicRule.setProduct_id(dynamicRuleRequest.getProductId());
        dynamicRule.setProduct_text(dynamicRuleRequest.getProductText());
        // Получение списка Rule из RuleRequest и его связывание c DynamicRule
        List<Rule> rules = dynamicRuleRequest.getRules().stream()
                .map(ruleRequest -> {
                    Rule rule = new Rule();
                    rule.setQuery(ruleRequest.getQuery());
                    rule.setArguments(ruleRequest.getArguments());
                    rule.setNegate(ruleRequest.isNegate());
                    // Связывание двух сущностей
                    rule.setDynamicRule(dynamicRule);
                    return rule;
                })
                .collect(Collectors.toList());
        dynamicRule.setRule(rules);
        DynamicRule result = ruleService.createDynamicRule(dynamicRule);
        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    // Удаление динамического правила
    @DeleteMapping("/{product_id}")
    public ResponseEntity<Void> deleteDynamicRules(@PathParam("product_id") String productId) {
        ruleService.deleteDynamicRules(productId);
        return ResponseEntity.noContent().build();
    }
}
