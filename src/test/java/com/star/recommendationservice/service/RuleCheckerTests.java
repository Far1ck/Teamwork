package com.star.recommendationservice.service;

import com.star.recommendationservice.error.UserNotFoundException;
import com.star.recommendationservice.model.DynamicRule;
import com.star.recommendationservice.model.Rule;
import com.star.recommendationservice.repository.RecommendationsServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RuleCheckerTests {

    @Mock
    private RecommendationsServiceRepository recommendationsServiceRepository;

    @InjectMocks
    private RuleChecker ruleChecker;

    @Test
    public void userIsExistCheckTest_UserIsExist() {
        UUID uuid = UUID.randomUUID();

        when(recommendationsServiceRepository.userIsExist(any(UUID.class))).thenReturn(1);

        ruleChecker.userIsExistCheck(uuid);

        verify(recommendationsServiceRepository, times(1)).userIsExist(any(UUID.class));
    }

    @Test
    public void userIsExistCheckTest_UserIsNotExist() {
        UUID uuid = UUID.randomUUID();

        when(recommendationsServiceRepository.userIsExist(any(UUID.class))).thenReturn(0);

        assertThrows(UserNotFoundException.class, () -> ruleChecker.userIsExistCheck(uuid));
        verify(recommendationsServiceRepository, times(1)).userIsExist(any(UUID.class));
    }

    @Test
    public void getUserNameTest_ReturnUserName() {
        UUID uuid = UUID.randomUUID();
        String name = "Farit Salyakhov";

        when(recommendationsServiceRepository.getUserName(any(UUID.class))).thenReturn(name);

        String result = ruleChecker.getUserName(uuid);

        assertEquals(name, result);
        verify(recommendationsServiceRepository, times(1)).getUserName(any(UUID.class));
    }

    @Test
    public void ruleCheckTest_Success() {
        UUID id = UUID.randomUUID();
        DynamicRule dynamicRule = new DynamicRule();
        Rule rule1 = new Rule();
        rule1.setId(1L);
        rule1.setDynamicRule(dynamicRule);
        rule1.setQuery("USER_OF");
        rule1.setArguments(List.of("DEBIT"));
        rule1.setNegate(false);
        Rule rule2 = new Rule();
        rule2.setId(2L);
        rule2.setDynamicRule(dynamicRule);
        rule2.setQuery("USER_O");
        rule2.setArguments(List.of("DEBIT"));
        rule2.setNegate(false);
        Rule rule3 = new Rule();
        rule3.setId(3L);
        rule3.setDynamicRule(dynamicRule);
        rule3.setQuery("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW");
        rule3.setArguments(List.of("DEBIT", ">"));
        rule3.setNegate(false);
        Rule rule4 = new Rule();
        rule4.setId(4L);
        rule4.setDynamicRule(dynamicRule);
        rule4.setQuery("TRANSACTION_SUM_COMPARE");
        rule4.setArguments(List.of("DEBIT", "DEPOSIT", ">", "1000"));
        rule4.setNegate(false);

        when(recommendationsServiceRepository.userOfCheck(any(UUID.class), anyString())).thenReturn(1);
        when(recommendationsServiceRepository.userTransactionsSum(any(UUID.class), anyString(), eq("DEPOSIT"))).thenReturn(1500);
        when(recommendationsServiceRepository.userTransactionsSum(any(UUID.class), anyString(), eq("WITHDRAW"))).thenReturn(500);

        int result1 = ruleChecker.ruleCheck(id, rule1);
        int result2 = ruleChecker.ruleCheck(id, rule2);
        int result3 = ruleChecker.ruleCheck(id, rule3);
        int result4 = ruleChecker.ruleCheck(id, rule4);

        assertEquals(1, result1);
        assertEquals(0, result2);
        assertEquals(1, result3);
        assertEquals(1, result4);
    }
}
