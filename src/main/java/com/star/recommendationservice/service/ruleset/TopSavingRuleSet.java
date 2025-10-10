package com.star.recommendationservice.service.ruleset;

import com.star.recommendationservice.model.Recommendation;
import com.star.recommendationservice.repository.RecommendationsServiceRepository;

import java.util.Optional;
import java.util.UUID;

public class TopSavingRuleSet implements RecommendationRuleSet {

    private final RecommendationsServiceRepository recommendationsServiceRepository;

    public TopSavingRuleSet(RecommendationsServiceRepository recommendationsServiceRepository) {
        this.recommendationsServiceRepository = recommendationsServiceRepository;
    }

    @Override
    public Optional<Recommendation> checkRecommendationRuleSet(UUID userId) {
        int debitDepositSum = recommendationsServiceRepository.getDebitDepositSum(userId);
        if (recommendationsServiceRepository.debitTransactionIsExist(userId) > 0
                && (debitDepositSum >= 50000 || recommendationsServiceRepository.getSavingDepositSum(userId) >= 50000)
                && debitDepositSum > recommendationsServiceRepository.getDebitWithdrawSum(userId)) {
            return Optional.of(new Recommendation("Top Saving", UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"),
                    """
                    Откройте свою собственную \
                    «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет \
                    вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и \
                    потерянных квитанций — всё под контролем!
                    
                    Преимущества «Копилки»:
                    
                    Накопление средств на конкретные цели. Установите лимит и срок накопления, \
                    и банк будет автоматически переводить определенную сумму на ваш счет.
                    
                    Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте \
                    процесс накопления и корректируйте стратегию при необходимости.
                    
                    Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним \
                    возможен только через мобильное приложение или интернет-банкинг.
                    
                    Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!"""));
        }
        return Optional.empty();
    }
}
