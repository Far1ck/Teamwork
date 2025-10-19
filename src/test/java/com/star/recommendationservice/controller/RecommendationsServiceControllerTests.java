/*package com.star.recommendationservice.controller;

import com.star.recommendationservice.model.Recommendation;
import com.star.recommendationservice.model.UserRecommendations;
import com.star.recommendationservice.repository.RecommendationsServiceRepository;
import com.star.recommendationservice.service.RecommendationsService;
import com.star.recommendationservice.service.ruleset.Invest500RuleSet;
import com.star.recommendationservice.service.ruleset.SimpleCreditRuleSet;
import com.star.recommendationservice.service.ruleset.TopSavingRuleSet;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendationsServiceController.class)
public class RecommendationsServiceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationsServiceRepository recommendationsServiceRepository;

    @SpyBean
    private RecommendationsService recommendationsService;

    @SpyBean
    private Invest500RuleSet invest500RuleSet;

    @SpyBean
    private TopSavingRuleSet topSavingRuleSet;

    @SpyBean
    private SimpleCreditRuleSet simpleCreditRuleSet;

    @InjectMocks
    private RecommendationsServiceController recommendationsServiceController;

    private final String text1 = """
                            Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! \
                            Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до \
                            конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. \
                            Не упустите возможность разнообразить свой портфель, снизить риски и следить за \
                            актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!""";
    private final String text2 = """
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
                    
                    Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!""";
    private final String text3 = """
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
                            
                            Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!""";


    @Test
    public void getUserRecommendationsTest_FirstRuleSetSuccess() throws Exception {
        UUID userId = UUID.randomUUID();
        Recommendation recommendation = new Recommendation("Invest 500", UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"), text1);
        List<Recommendation> list = new ArrayList<>(1);
        list.add(recommendation);
        UserRecommendations result = new UserRecommendations(userId, list);

        when(recommendationsServiceRepository.userIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.debitTransactionIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.investTransactionIsExist(any(UUID.class))).thenReturn(0);
        when(recommendationsServiceRepository.getSavingDepositSum(any(UUID.class))).thenReturn(1001);
        when(recommendationsServiceRepository.getDebitDepositSum(any(UUID.class))).thenReturn(1000);
        when(recommendationsServiceRepository.getDebitWithdrawSum(any(UUID.class))).thenReturn(700);
        when(recommendationsServiceRepository.creditTransactionIsExist(any(UUID.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recommendation/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(("$.user_id")).value(result.getUser_id().toString()))
                .andExpect(jsonPath("$.recommendations[0].name").value(recommendation.getName()))
                .andExpect(jsonPath(("$.recommendations[0].id")).value(recommendation.getId().toString()))
                .andExpect(jsonPath("$.recommendations[0].text").value(recommendation.getText()));

    }

    @Test
    public void getUserRecommendationsTest_SecondRuleSetSuccess() throws Exception {
        UUID userId = UUID.randomUUID();
        Recommendation recommendation = new Recommendation("Top Saving", UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"), text2);
        List<Recommendation> list = new ArrayList<>(2);
        list.add(recommendation);
        UserRecommendations result = new UserRecommendations(userId, list);

        when(recommendationsServiceRepository.userIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.debitTransactionIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.investTransactionIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.getSavingDepositSum(any(UUID.class))).thenReturn(1001);
        when(recommendationsServiceRepository.getDebitDepositSum(any(UUID.class))).thenReturn(50001);
        when(recommendationsServiceRepository.getDebitWithdrawSum(any(UUID.class))).thenReturn(700);
        when(recommendationsServiceRepository.creditTransactionIsExist(any(UUID.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recommendation/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(("$.user_id")).value(result.getUser_id().toString()))
                .andExpect(jsonPath("$.recommendations[0].name").value(recommendation.getName()))
                .andExpect(jsonPath(("$.recommendations[0].id")).value(recommendation.getId().toString()))
                .andExpect(jsonPath("$.recommendations[0].text").value(recommendation.getText()));

    }

    @Test
    public void getUserRecommendationsTest_SecondAndThirdRuleSetSuccess() throws Exception {
        UUID userId = UUID.randomUUID();
        Recommendation recommendation1 = new Recommendation("Top Saving", UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"), text2);
        Recommendation recommendation2 = new Recommendation("Простой кредит", UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f"), text3);
        List<Recommendation> list = new ArrayList<>(2);
        list.add(recommendation1);
        list.add(recommendation2);
        UserRecommendations result = new UserRecommendations(userId, list);

        when(recommendationsServiceRepository.userIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.debitTransactionIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.investTransactionIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.getSavingDepositSum(any(UUID.class))).thenReturn(1001);
        when(recommendationsServiceRepository.getDebitDepositSum(any(UUID.class))).thenReturn(120000);
        when(recommendationsServiceRepository.getDebitWithdrawSum(any(UUID.class))).thenReturn(100001);
        when(recommendationsServiceRepository.creditTransactionIsExist(any(UUID.class))).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recommendation/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(("$.user_id")).value(result.getUser_id().toString()))
                .andExpect(jsonPath("$.recommendations[0].name").value(recommendation1.getName()))
                .andExpect(jsonPath(("$.recommendations[0].id")).value(recommendation1.getId().toString()))
                .andExpect(jsonPath("$.recommendations[0].text").value(recommendation1.getText()))
                .andExpect(jsonPath("$.recommendations[1].text").value(recommendation2.getText()))
                .andExpect(jsonPath(("$.recommendations[1].id")).value(recommendation2.getId().toString()))
                .andExpect(jsonPath("$.recommendations[1].name").value(recommendation2.getName()));
    }

    @Test
    public void getUserRecommendationsTest_NoRecommendations() throws Exception {
        UUID userId = UUID.randomUUID();

        when(recommendationsServiceRepository.userIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.debitTransactionIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.investTransactionIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.getSavingDepositSum(any(UUID.class))).thenReturn(1001);
        when(recommendationsServiceRepository.getDebitDepositSum(any(UUID.class))).thenReturn(500);
        when(recommendationsServiceRepository.getDebitWithdrawSum(any(UUID.class))).thenReturn(700);
        when(recommendationsServiceRepository.creditTransactionIsExist(any(UUID.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recommendation/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(("$.user_id")).value(userId.toString()))
                .andExpect(jsonPath("$.recommendations").isEmpty());
    }

    @Test
    public void getUserRecommendationsTest_UserNotExist() throws Exception {
        UUID userId = UUID.randomUUID();

        when(recommendationsServiceRepository.userIsExist(any(UUID.class))).thenReturn(0);
        when(recommendationsServiceRepository.debitTransactionIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.investTransactionIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.getSavingDepositSum(any(UUID.class))).thenReturn(1001);
        when(recommendationsServiceRepository.getDebitDepositSum(any(UUID.class))).thenReturn(500);
        when(recommendationsServiceRepository.getDebitWithdrawSum(any(UUID.class))).thenReturn(700);
        when(recommendationsServiceRepository.creditTransactionIsExist(any(UUID.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recommendation/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}*/