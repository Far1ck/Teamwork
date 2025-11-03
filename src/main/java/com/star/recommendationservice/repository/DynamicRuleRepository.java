package com.star.recommendationservice.repository;

import com.star.recommendationservice.model.Counter;
import com.star.recommendationservice.model.DynamicRule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, Long> {
    /**
     * Метод для удаления динамического правила (продукта) из базы по его ID
     * @param product_id ID продукта, который мы удаляем
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM dynamic_rules WHERE product_id = :product_id")
    void deleteByProductId(String product_id);

    /**
     * Метод проверки существования пользователя Telegram-бота в базе данных
     * @param userId ID пользователя, которого мы проверяем
     * @return Возвращает 1, если пользователь существует, 0 - если не существует
     */
    @Query(nativeQuery = true, value = "SELECT COUNT(user_id) FROM users WHERE user_id = :userId")
    int userIsExist(String userId);

    /**
     * Метод для добавления нового пользователя Telegram-бота в базу данных
     * @param userId ID пользователя, которого мы добавляем
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO users (user_id) VALUES (:userId)")
    void addNewUser(String userId);

    /**
     * Метод для удаления счетчика срабатываний для динамичесвого правила (продукта)
     * @param productId ID динамического правила (продукта), счетчик которого мы удаляем
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM response_counters WHERE rule_id = :productId")
    void deleteProductCounter(String productId);

    /**
     * Метод для добавления счетчика для динамического правила (продукта)
     * @param productId ID динамического правила (продукта), которое мы добавляем
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO response_counters (rule_id) VALUES (:productId)")
    void addProductCounter(String productId);

    /**
     * Метод для получения счетчиков всех динамических правил
     * @return Лист счетчиков
     */
    @Query(nativeQuery = true, value = "SELECT * FROM response_counters")
    List<Counter> getAllCounters();

    /**
     * Метод для увеличения счетчика срабатывания, если динамическое правило сработало
     * @param productId ID сработавшего динамического правила
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE response_counters SET counter = counter + 1 WHERE rule_id = :productId")
    void increaseCounter(String productId);
}
