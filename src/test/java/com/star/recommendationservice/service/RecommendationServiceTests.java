package com.star.recommendationservice.service;

import com.star.recommendationservice.model.DynamicRule;
import com.star.recommendationservice.model.Rule;
import com.star.recommendationservice.model.recommendation.UserRecommendations;
import com.star.recommendationservice.repository.DynamicRuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTests {

    @Mock
    private RuleChecker ruleChecker;

    @Mock
    private DynamicRuleRepository dynamicRuleRepository;

    @InjectMocks
    private RecommendationsService recommendationsService;

    @Test
    public void getUserRecommendationsTest() {
        UUID id = UUID.randomUUID();
        DynamicRule dynamicRule = new DynamicRule();
        dynamicRule.setId(1L);
        dynamicRule.setProduct_name("product_name");
        dynamicRule.setProduct_id("59efc529-2fff-41af-baff-90ccd7402925");
        dynamicRule.setProduct_text("text");
        dynamicRule.setRule(List.of(new Rule(), new Rule(), new Rule()));
        List<DynamicRule> dynamicRules = List.of(dynamicRule);

        doNothing().when(ruleChecker).userIsExistCheck(any(UUID.class));
        when(dynamicRuleRepository.findAll()).thenReturn(dynamicRules);
        when(ruleChecker.ruleCheck(any(UUID.class), any(Rule.class))).thenReturn(1);
        doNothing().when(dynamicRuleRepository).increaseCounter(anyString());

        UserRecommendations result = recommendationsService.getUserRecommendations(id);

        assertEquals(id, result.getUser_id());
        assertEquals(1, result.getRecommendations().size());
        assertEquals(dynamicRule.getProduct_name(), result.getRecommendations().get(0).getName());
        assertEquals(UUID.fromString(dynamicRule.getProduct_id()), result.getRecommendations().get(0).getId());
        assertEquals(dynamicRule.getProduct_text(), result.getRecommendations().get(0).getText());
        verify(ruleChecker, times(1)).userIsExistCheck(any(UUID.class));
        verify(dynamicRuleRepository, times(1)).findAll();
        verify(ruleChecker, times(3)).ruleCheck(any(UUID.class), any(Rule.class));
    }
}
