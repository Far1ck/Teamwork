package com.star.recommendationservice.controller;

import com.star.recommendationservice.model.*;
import com.star.recommendationservice.service.DynamicRuleService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
public class DynamicRuleController {
    private final DynamicRuleService dynamicRuleService;

    public DynamicRuleController(DynamicRuleService dynamicRuleService) {
        this.dynamicRuleService = dynamicRuleService;
    }

    // Получение всех динамических правил
    @GetMapping
    public ResponseEntity<DynamicRulesList> getAllRules() {
        DynamicRulesList listing = new DynamicRulesList(dynamicRuleService.getAllRules());
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
        List<Rule> rules = dynamicRuleRequest.getRule().stream()
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
        DynamicRule result = dynamicRuleService.createDynamicRule(dynamicRule);
        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    // Удаление динамического правила
    @DeleteMapping("/{product_id}")
    public ResponseEntity<Void> deleteDynamicRule(@PathParam("product_id") String productId) {
        dynamicRuleService.deleteDynamicRules(productId);
        return ResponseEntity.noContent().build();
    }

    // Возвращает статистику срабатывания правил
    @GetMapping("/stats")
    public ResponseEntity<Stats> getStats() {
        Stats result = new Stats(dynamicRuleService.getAllCounters());
        return ResponseEntity.ok(result);
    }
}
