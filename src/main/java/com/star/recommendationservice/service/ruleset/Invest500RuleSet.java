package com.star.recommendationservice.service.ruleset;

import com.star.recommendationservice.repository.RecommendationsServiceRepository;
import org.springframework.stereotype.Component;
import com.star.recommendationservice.model.Recommendation;

import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500RuleSet implements RecommendationRuleSet {

    private final RecommendationsServiceRepository recommendationsServiceRepository;

    public Invest500RuleSet(RecommendationsServiceRepository recommendationsServiceRepository) {
        this.recommendationsServiceRepository = recommendationsServiceRepository;
    }

    @Override
    public Optional<Recommendation> checkRecommendationRuleSet(UUID userId) {
        if (recommendationsServiceRepository.debitTransactionIsExist(userId) > 0
        && recommendationsServiceRepository.investTransactionIsExist(userId) == 0
        && recommendationsServiceRepository.getSavingDepositSum(userId) > 1000) {
            return Optional.of(new Recommendation(
                    "Invest 500", UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"),
                    """
                            Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! \
                            Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до \
                            конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. \
                            Не упустите возможность разнообразить свой портфель, снизить риски и следить за \
                            актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!"""));
        }
        return Optional.empty();
    }
}
