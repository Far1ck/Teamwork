package com.star.recommendationservice.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsServiceRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsServiceRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int userIsExist(UUID userId) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT COUNT(t.USER_ID)
                        FROM TRANSACTIONS AS t
                        WHERE t.USER_ID = ?
                        LIMIT 1""", Integer.class, userId);
        return result != null ? result : 0;
    }

    public int debitTransactionIsExist(UUID userId) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT COUNT(i)
                        FROM (SELECT t.ID AS i FROM TRANSACTIONS AS t
                        JOIN PRODUCTS AS p ON t.PRODUCT_ID = p.ID
                        WHERE t.USER_ID = ? AND p."TYPE" LIKE 'DEBIT'
                        LIMIT 1)""", Integer.class, userId);
        return result != null ? result : 0;
    }

    public int investTransactionIsExist(UUID userId) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT COUNT(i)
                        FROM (SELECT t.ID AS i FROM TRANSACTIONS AS t
                        JOIN PRODUCTS AS p ON t.PRODUCT_ID = p.ID
                        WHERE t.USER_ID = ? AND p."TYPE" LIKE 'INVEST'
                        LIMIT 1)""", Integer.class, userId);
        return result != null ? result : 0;
    }

    public int creditTransactionIsExist(UUID userId) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT COUNT(i)
                        FROM (SELECT t.ID AS i FROM TRANSACTIONS AS t
                        JOIN PRODUCTS AS p ON t.PRODUCT_ID = p.ID
                        WHERE t.USER_ID = ? AND p."TYPE" LIKE 'CREDIT'
                        LIMIT 1)""", Integer.class, userId);
        return result != null ? result : 0;
    }

    public int getSavingDepositSum(UUID userId) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT SUM(t.AMOUNT) FROM TRANSACTIONS AS t
                        JOIN PRODUCTS AS p ON t.PRODUCT_ID = p.ID
                        WHERE t.USER_ID = ? AND p."TYPE" LIKE 'SAVING' AND t."TYPE" LIKE 'DEPOSIT'""",
                Integer.class, userId);
        return result != null ? result : 0;
    }

    public int getDebitDepositSum(UUID userId) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT SUM(t.AMOUNT) FROM TRANSACTIONS AS t
                        JOIN PRODUCTS AS p ON t.PRODUCT_ID = p.ID
                        WHERE t.USER_ID = ? AND p."TYPE" LIKE 'DEBIT' AND t."TYPE" LIKE 'DEPOSIT'""",
                Integer.class, userId);
        return result != null ? result : 0;
    }

    public int getDebitWithdrawSum(UUID userId) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT SUM(t.AMOUNT) FROM TRANSACTIONS AS t
                        JOIN PRODUCTS AS p ON t.PRODUCT_ID = p.ID
                        WHERE t.USER_ID = ? AND p."TYPE" LIKE 'DEBIT' AND t."TYPE" LIKE 'WITHDRAW'""",
                Integer.class, userId);
        return result != null ? result : 0;
    }
}
