package com.star.recommendationservice.service.ruleset;

import com.star.recommendationservice.model.Recommendation;
import com.star.recommendationservice.repository.RecommendationsServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SimpleCreditRuleSetTest {

    @InjectMocks
    private SimpleCreditRuleSet ruleSet;

    @Mock
    private RecommendationsServiceRepository repository;

    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
    }

    //Позитивный тест - все условия соблюдены
    @Test
    void testRuleSatisfied() {
        when(repository.getDebitWithdrawSum(testUserId)).thenReturn(120000);
        when(repository.creditTransactionIsExist(testUserId)).thenReturn(0);
        when(repository.getDebitDepositSum(testUserId)).thenReturn(150000);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertTrue(result.isPresent());
        Recommendation rec = result.get();

        assertEquals("Простой кредит", rec.getName());
        assertEquals("ab138afb-f3ba-4a93-b74f-0fcee86d447f", rec.getId().toString());
        assertNotNull(rec.getText());
    }

    //Негативный тест - уже есть кредитные транзакции
    @Test
    void testCreditTransactionsExist() {
        when(repository.creditTransactionIsExist(testUserId)).thenReturn(1);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertFalse(result.isPresent());
    }

    // Негативный тест - недостаточно депозитов
    @Test
    void testInsufficientDebitDeposit() {
        when(repository.creditTransactionIsExist(testUserId)).thenReturn(0);
        when(repository.getDebitDepositSum(testUserId)).thenReturn(100000);
        when(repository.getDebitWithdrawSum(testUserId)).thenReturn(90000);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertFalse(result.isPresent());
    }

    // Негативный тест - мало снятий
    @Test
    void testLowWithdrawSum() {
        when(repository.creditTransactionIsExist(testUserId)).thenReturn(0);
        when(repository.getDebitDepositSum(testUserId)).thenReturn(150000);
        when(repository.getDebitWithdrawSum(testUserId)).thenReturn(90000);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertFalse(result.isPresent());
    }

}
