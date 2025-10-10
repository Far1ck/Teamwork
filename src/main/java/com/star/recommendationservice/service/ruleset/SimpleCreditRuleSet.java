package com.star.recommendationservice.service.ruleset;

import com.star.recommendationservice.model.Recommendation;
import com.star.recommendationservice.repository.RecommendationsServiceRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {

    private final RecommendationsServiceRepository recommendationsServiceRepository;

    public SimpleCreditRuleSet(RecommendationsServiceRepository recommendationsServiceRepository) {
        this.recommendationsServiceRepository = recommendationsServiceRepository;
    }

    @Override
    public Optional<Recommendation> checkRecommendationRuleSet(UUID userId) {
        int debitWithdrawSum = recommendationsServiceRepository.getDebitWithdrawSum(userId);
        if (recommendationsServiceRepository.creditTransactionIsExist(userId) == 0
        && recommendationsServiceRepository.getDebitDepositSum(userId) > debitWithdrawSum
        && debitWithdrawSum > 100000) {
            return Optional.of(new Recommendation(
                    "Простой кредит", UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f"),
                    """
                            Откройте мир выгодных кредитов с нами!
                            
                            Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный \
                            кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие \
                            условия и индивидуальный подход к каждому клиенту.
                            
                            Почему выбирают нас:
                            
                            Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения \
                            заявки занимает всего несколько часов.
                            
                            Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.
                            
                            Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: \
                            покупку недвижимости, автомобиля, образование, лечение и многое другое.
                            
                            Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!"""));
        }
        return Optional.empty();
    }
}
