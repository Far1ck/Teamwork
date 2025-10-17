package com.star.recommendationservice.repository;

import com.star.recommendationservice.model.DynamicRule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, Long> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM dynamic_rules WHERE product_id = :product_id")
    void deleteByProductId(String product_id);
}
