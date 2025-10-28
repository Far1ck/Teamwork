package com.star.recommendationservice.repository;

import com.star.recommendationservice.model.Counter;
import com.star.recommendationservice.model.DynamicRule;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, Long> {
    // Запрос в базу для удаления всех динамических правил с полученным ID продукта
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM dynamic_rules WHERE product_id = :product_id")
    void deleteByProductId(String product_id);

    @Query(nativeQuery = true, value = "SELECT COUNT(user_id) FROM users WHERE user_id = :userId")
    int userIsExist(String userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO users (user_id) VALUES (:userId)")
    void addNewUser(String userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM response_counters WHERE rule_id = :productId")
    void deleteProductCounter(String productId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO response_counters (rule_id) VALUES (:productId)")
    void addProductCounter(String productId);

    @Query(nativeQuery = true, value = "SELECT * FROM response_counters")
    List<Counter> getAllCounters();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE response_counters SET counter = counter + 1 WHERE rule_id = :productId")
    void increaseCounter(String productId);
}
