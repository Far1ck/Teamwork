package com.star.recommendationservice.service;

import com.star.recommendationservice.model.Recommendation;
import com.star.recommendationservice.model.UserRecommendations;
import com.star.recommendationservice.service.ruleset.RecommendationRuleSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationsServiceTest {

    @Mock
    private RecommendationRuleSet ruleSet1;

    @Mock
    private RecommendationRuleSet ruleSet2;

    private RecommendationsService service;

    @BeforeEach
    void setUp() {
        // Создаем список правил и передаем его в конструктор
        service = new RecommendationsService(List.of(ruleSet1, ruleSet2));
    }


    @Test
    void testGetUserRecommendations() {
        UUID userId = UUID.randomUUID();

        // Создаем тестовую рекомендацию
        Recommendation testRecommendation = new Recommendation();

        // Настраиваем поведение моков
        when(ruleSet1.checkRecommendationRuleSet(userId))
                .thenReturn(Optional.of(testRecommendation));

        when(ruleSet2.checkRecommendationRuleSet(userId))
                .thenReturn(Optional.empty());

        // Выполняем тестируемый метод
        UserRecommendations result = service.getUserRecommendations(userId);

        // Проверяем результат
        assertEquals(1, result.getRecommendations().size(), "Должна быть одна рекомендация");

        // Дополнительные проверки
        List<Recommendation> recommendations = result.getRecommendations();
        assertTrue(recommendations.contains(testRecommendation),
                "Рекомендация должна совпадать с ожидаемой");
    }
}





