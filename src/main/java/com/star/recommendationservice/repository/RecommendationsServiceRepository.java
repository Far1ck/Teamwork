package com.star.recommendationservice.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.UUID;

@Repository
public class RecommendationsServiceRepository {
    private final JdbcTemplate jdbcTemplate;

    private final Cache<String, Integer> userOfCheckCache;
    private final Cache<String, Integer> activeUserOfCheckCache;
    private final Cache<String, Integer> transactionsSumCache;



    public RecommendationsServiceRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        userOfCheckCache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(5))
                .maximumSize(1000)
                .build();

        activeUserOfCheckCache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(5))
                .maximumSize(1000)
                .build();

        transactionsSumCache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(5))
                .maximumSize(1000)
                .build();
    }

    /**
     * Запрос в базу данных для проверки существования в ней пользователя
     * @param userId ID пользователя, которого мы проверяем
     * @return Возвращает 1, если пользователь есть, 0 - если пользователя нет или их больше 1
     */
    public int userIsExist(UUID userId) {
        Integer result = jdbcTemplate.queryForObject(
                """
                        SELECT COUNT(ID)
                        FROM USERS
                        WHERE ID = ?
                        LIMIT 2""", Integer.class, userId);
        return result != null && result == 1 ? result : 0;
    }

    /**
     * Запрос в БД для получения полного имени пользователя
     * @param userId ID пользователя, имя которого мы хотим получить
     * @return Возвращает строку с полным именем пользователя
     */
    public String getUserName(UUID userId) {
        String firstName = jdbcTemplate.queryForObject(
                """
                        SELECT FIRST_NAME
                        FROM USERS
                        WHERE ID = ?""", String.class, userId
        );
        String lastName = jdbcTemplate.queryForObject(
                """
                        SELECT LAST_NAME
                        FROM USERS
                        WHERE ID = ?""", String.class, userId
        );
        return firstName + " " + lastName;
    }

    /**
     * Запрос в БД (если нет в кэше) для поиска хотя бы одной транзакции пользователя для продукта полученного типа
     * @param userId ID пользователя, для которого ищется транзакция
     * @param productType Тип продукта, по которому ищется транзакция
     * @return Возвращает 1, если транзакция есть, 0 - если нет
     */
    public int userOfCheck(UUID userId, String productType) {
        String cacheKey = userId + "|" + productType;
        return userOfCheckCache.get(cacheKey, (key) -> {
            Integer result = jdbcTemplate.queryForObject(
                    """
                            SELECT COUNT(i)
                            FROM (SELECT t.ID AS i FROM TRANSACTIONS AS t
                            JOIN PRODUCTS AS p ON t.PRODUCT_ID = p.ID
                            WHERE t.USER_ID = ? AND p."TYPE" LIKE ?
                            LIMIT 1)""", Integer.class, userId, productType);
            return result != null ? result : 0;
        });
    }

    /**
     * Запрос в БД (если нет в кэше) для поиска хотя бы пяти транзакций пользователя для продукта полученного типа
     * @param userId ID пользователя, для которого ищутся транзакции
     * @param productType Тип продукта, для которого ищутся транзакции
     * @return Возвращает 1, если найдено 5 или больше транзакций, 0 - если меньше
     */
    public int activeUserOfCheck(UUID userId, String productType) {
        String cacheKey = userId + "|" + productType;
        return activeUserOfCheckCache.get(cacheKey, (key) -> {
            Integer result = jdbcTemplate.queryForObject(
                    """
                            SELECT COUNT(i)
                            FROM (SELECT t.ID AS i FROM TRANSACTIONS AS t
                            JOIN PRODUCTS AS p ON t.PRODUCT_ID = p.ID
                            WHERE t.USER_ID = ? AND p."TYPE" LIKE ?
                            LIMIT 5)""", Integer.class, userId, productType);
            return (result != null)&&(result == 5) ? 1 : 0;
        });
    }

    /**
     * Запрос в БД (если нет в кэше) для получения суммы всех транзакций пользователя полученного типа
     * для продукта полученного типа
     * @param userId ID пользователя, для которого вычисляется сумма
     * @param productType Тип продукта, для которого вычисляется сумма
     * @param transactionType Тип трназакций, сумма которых вычисляется
     * @return Возвращает сумму транзакций
     */
    public int userTransactionsSum(UUID userId, String productType, String transactionType) {
        String cacheKey = userId + "|" + productType + "|" + transactionType;
        return transactionsSumCache.get(cacheKey, (key) -> {
            Integer result = jdbcTemplate.queryForObject(
                    """
                            SELECT SUM(t.AMOUNT) FROM TRANSACTIONS AS t
                            JOIN PRODUCTS AS p ON t.PRODUCT_ID = p.ID
                            WHERE t.USER_ID = ? AND p."TYPE" LIKE ? AND t."TYPE" LIKE ?""",
                    Integer.class, userId, productType, transactionType);
            return result != null ? result : 0;
        });
    }

    /**
     * Метод для очистки кэша
     */
    public void clearAllCaches() {
        userOfCheckCache.invalidateAll();
        activeUserOfCheckCache.invalidateAll();
        transactionsSumCache.invalidateAll();
    }
}
