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
class Invest500RuleSetTest {

    @InjectMocks
    private Invest500RuleSet ruleSet;

    @Mock
    private RecommendationsServiceRepository repository;

    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
    }

    // Позитивный тест - все условия соблюдены
    @Test
    void testRuleSatisfied() {
        when(repository.debitTransactionIsExist(testUserId)).thenReturn(1);
        when(repository.investTransactionIsExist(testUserId)).thenReturn(0);
        when(repository.getSavingDepositSum(testUserId)).thenReturn(1500);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertTrue(result.isPresent());
        Recommendation rec = result.get();

        assertEquals("Invest 500", rec.getName());
        assertEquals("147f6a0f-3b91-413b-ab99-87f081d60d5a", rec.getId().toString());
        assertNotNull(rec.getText());
    }

    // Негативный тест - нет дебетовых транзакций
    @Test
    void testNoDebitTransactions() {
        when(repository.debitTransactionIsExist(testUserId)).thenReturn(0);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertFalse(result.isPresent());
    }

    // Негативный тест - уже есть инвестиционные транзакции
    @Test
    void testInvestTransactionsExist() {
        when(repository.debitTransactionIsExist(testUserId)).thenReturn(1);
        when(repository.investTransactionIsExist(testUserId)).thenReturn(1);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertFalse(result.isPresent());
    }

    // Негативный тест - недостаточно сбережений
    @Test
    void testInsufficientSavingDeposit() {
        when(repository.debitTransactionIsExist(testUserId)).thenReturn(1);
        when(repository.investTransactionIsExist(testUserId)).thenReturn(0);
        when(repository.getSavingDepositSum(testUserId)).thenReturn(500);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertFalse(result.isPresent());
    }

    // Граничный случай - ровно 1000 на сберегательном счете
    @Test
    void testMinimumSavingDeposit() {
        when(repository.debitTransactionIsExist(testUserId)).thenReturn(1);
        when(repository.investTransactionIsExist(testUserId)).thenReturn(0);
        when(repository.getSavingDepositSum(testUserId)).thenReturn(1000);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertFalse(result.isPresent());
    }

    // Граничный случай - чуть больше 1000 на сберегательном счете
    @Test
    void testJustAboveMinimumSavingDeposit() {
        when(repository.debitTransactionIsExist(testUserId)).thenReturn(1);
        when(repository.investTransactionIsExist(testUserId)).thenReturn(0);
        when(repository.getSavingDepositSum(testUserId)).thenReturn(1001);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertTrue(result.isPresent());
    }
}
