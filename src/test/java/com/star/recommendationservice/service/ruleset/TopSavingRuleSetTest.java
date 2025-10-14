package com.star.recommendationservice.service.ruleset;

import com.star.recommendationservice.model.Recommendation;
import com.star.recommendationservice.repository.RecommendationsServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopSavingRuleSetTest {

    @InjectMocks
    private TopSavingRuleSet ruleSet;

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
        Mockito.lenient()
                .when(repository.debitTransactionIsExist(testUserId))
                .thenReturn(1);

        Mockito.lenient()
                .when(repository.getDebitDepositSum(testUserId))
                .thenReturn(60000);

        Mockito.lenient()
                .when(repository.getSavingDepositSum(testUserId))
                .thenReturn(40000);

        Mockito.lenient()
                .when(repository.getDebitWithdrawSum(testUserId))
                .thenReturn(50000);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertTrue(result.isPresent());
        Recommendation rec = result.get();

        assertEquals("Top Saving", rec.getName());
        assertEquals("59efc529-2fff-41af-baff-90ccd7402925", rec.getId().toString());
        assertNotNull(rec.getText());
    }

    // Негативный тест - нет дебетовых транзакций
    @Test
    void testNoDebitTransactions() {
        when(repository.debitTransactionIsExist(testUserId)).thenReturn(0);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertFalse(result.isPresent());
    }

    // Негативный тест - недостаточно средств на счетах
    @Test
    void testInsufficientDeposits() {
        when(repository.debitTransactionIsExist(testUserId)).thenReturn(1);
        when(repository.getDebitDepositSum(testUserId)).thenReturn(40000);
        when(repository.getSavingDepositSum(testUserId)).thenReturn(40000);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertFalse(result.isPresent());
    }

    // Граничный случай - ровно 50000 на дебетовом счете
    @Test
    void testMinimumDebitDeposit() {
        when(repository.debitTransactionIsExist(testUserId)).thenReturn(1);
        when(repository.getDebitDepositSum(testUserId)).thenReturn(50000);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertTrue(result.isPresent());
    }

    // Граничный случай - ровно 50000 на сберегательном счете
    @Test
    void testMinimumSavingDeposit() {
        when(repository.debitTransactionIsExist(testUserId)).thenReturn(1);
        when(repository.getDebitDepositSum(testUserId)).thenReturn(30000);
        when(repository.getSavingDepositSum(testUserId)).thenReturn(50000);
        when(repository.getDebitWithdrawSum(testUserId)).thenReturn(20000);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertTrue(result.isPresent());
    }

    // Негативный тест - превышены снятия над депозитами
    @Test
    void testWithdrawExceedsDeposit() {
        when(repository.debitTransactionIsExist(testUserId)).thenReturn(1);
        when(repository.getDebitDepositSum(testUserId)).thenReturn(50000);
        when(repository.getDebitWithdrawSum(testUserId)).thenReturn(60000);

        Optional<Recommendation> result = ruleSet.checkRecommendationRuleSet(testUserId);

        assertFalse(result.isPresent());
    }
}
