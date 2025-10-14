package com.star.recommendationservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RecommendationServiceRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private RecommendationsServiceRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDebitTransactionIsExist_Exists() {

        UUID userId = UUID.randomUUID();
        int expectedCount = 1;

        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(Integer.class),
                eq(userId)))
                .thenReturn(expectedCount);

        int result = repository.debitTransactionIsExist(userId);

        assertEquals(expectedCount, result);
        verify(jdbcTemplate).queryForObject(anyString(), eq(Integer.class), eq(userId));
    }

    @Test
    void testDebitTransactionIsExist_NotExists() {
        UUID userId = UUID.randomUUID();

        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(Integer.class),
                eq(userId)))
                .thenReturn(0);

        int result = repository.debitTransactionIsExist(userId);

        assertEquals(0, result);
    }

    @Test
    void testInvestTransactionIsExist_Exists() {
        UUID userId = UUID.randomUUID();

        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(Integer.class),
                eq(userId)))
                .thenReturn(1);

        int result = repository.investTransactionIsExist(userId);

        assertEquals(1, result);
    }

    @Test
    void testGetSavingDepositSum_Positive() {
        UUID userId = UUID.randomUUID();
        int expectedSum = 5000;

        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(Integer.class),
                eq(userId)))
                .thenReturn(expectedSum);

        int result = repository.getSavingDepositSum(userId);

        assertEquals(expectedSum, result);
    }

    @Test
    void testGetSavingDepositSum_Zero() {
        UUID userId = UUID.randomUUID();

        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(Integer.class),
                eq(userId)))
                .thenReturn(0);

        int result = repository.getSavingDepositSum(userId);

        assertEquals(0, result);
    }

    @Test
    void testNullResultHandling() {
        UUID userId = UUID.randomUUID();

        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(Integer.class),
                eq(userId)))
                .thenReturn(null);

        // Проверяем, что метод возвращает 0 при null результате
        assertEquals(0, repository.debitTransactionIsExist(userId));
        assertEquals(0, repository.getSavingDepositSum(userId));
    }

}
