package com.star.recommendationservice.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsServiceRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsServiceRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Запрос в базу для определения наличия пользователя с полученным ID
    // Если есть, возвращает 1, если нет - 0
    public int userIsExist(UUID userId) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT COUNT(t.USER_ID)
                        FROM TRANSACTIONS AS t
                        WHERE t.USER_ID = ?
                        LIMIT 1""", Integer.class, userId);
        return result != null ? result : 0;
    }

    // Запрос в базу для поиска хотя бы одной транзакции для продукта полученного типа
    // Если найдена, возвращает 1, если нет - 0
    @Cacheable("userOf")
    public int userOfCheck(UUID userId, String productType) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT COUNT(i)
                        FROM (SELECT t.ID AS i FROM TRANSACTIONS AS t
                        JOIN PRODUCTS AS p ON t.PRODUCT_ID = p.ID
                        WHERE t.USER_ID = ? AND p."TYPE" LIKE ?
                        LIMIT 1)""", Integer.class, userId, productType);
        return result != null ? result : 0;
    }

    // Запрос в базу для поиска хотя бы пяти транзакций для продукта полученного типа
    // Если найдена, возвращает 1, если нет - 0
    @Cacheable("activeUserOf")
    public int activeUserOfCheck(UUID userId, String productType) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT COUNT(i)
                        FROM (SELECT t.ID AS i FROM TRANSACTIONS AS t
                        JOIN PRODUCTS AS p ON t.PRODUCT_ID = p.ID
                        WHERE t.USER_ID = ? AND p."TYPE" LIKE ?
                        LIMIT 5)""", Integer.class, userId, productType);
        return (result != null)&&(result == 5) ? 1 : 0;
    }

    // Запрос в базу для получения суммы всех транзакций полученного типа для продукта полученного типа
    @Cacheable("transactionSum")
    public int userTransactionsSum(UUID userId, String productType, String transactionType) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT SUM(t.AMOUNT) FROM TRANSACTIONS AS t
                        JOIN PRODUCTS AS p ON t.PRODUCT_ID = p.ID
                        WHERE t.USER_ID = ? AND p."TYPE" LIKE ? AND t."TYPE" LIKE ?""",
                Integer.class, userId, productType, transactionType);
        return result != null ? result : 0;
    }
}
