package com.star.recommendationservice.controller;

import com.star.recommendationservice.model.DynamicRule;
import com.star.recommendationservice.model.recommendation.Recommendation;
import com.star.recommendationservice.model.Rule;
import com.star.recommendationservice.model.recommendation.UserRecommendations;
import com.star.recommendationservice.repository.DynamicRuleRepository;
import com.star.recommendationservice.repository.RecommendationsServiceRepository;
import com.star.recommendationservice.service.RecommendationsService;
import com.star.recommendationservice.service.RuleChecker;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendationsServiceController.class)
public class RecommendationsServiceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationsServiceRepository recommendationsServiceRepository;

    @MockBean
    private DynamicRuleRepository dynamicRuleRepository;

    @SpyBean
    private RecommendationsService recommendationsService;

    @SpyBean
    private RuleChecker ruleChecker;

    @InjectMocks
    private RecommendationsServiceController recommendationsServiceController;

    private final String text1 = """
                            Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! \
                            Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до \
                            конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. \
                            Не упустите возможность разнообразить свой портфель, снизить риски и следить за \
                            актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!""";
    @Test
    public void getUserRecommendationsTest_Success() throws Exception {
        UUID userId = UUID.randomUUID();
        Recommendation recommendation = new Recommendation("Invest 500", UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"), text1);
        List<Recommendation> list = new ArrayList<>(1);
        list.add(recommendation);
        UserRecommendations result = new UserRecommendations(userId, list);
        DynamicRule dynamicRule = new DynamicRule();
        dynamicRule.setId(1L);
        dynamicRule.setProduct_name("Invest 500");
        dynamicRule.setProduct_id("147f6a0f-3b91-413b-ab99-87f081d60d5a");
        dynamicRule.setProduct_text(text1);
        Rule rule = new Rule();
        Rule rule1 = new Rule();
        Rule rule2 = new Rule();
        rule.setId(1L);
        rule.setQuery("USER_OF");
        rule.setNegate(false);
        rule.setDynamicRule(dynamicRule);
        rule.setArguments(List.of("DEBIT"));
        rule1.setId(2L);
        rule1.setQuery("USER_OF");
        rule1.setNegate(true);
        rule1.setDynamicRule(dynamicRule);
        rule1.setArguments(List.of("INVEST"));
        rule2.setId(3L);
        rule2.setQuery("TRANSACTION_SUM_COMPARE");
        rule2.setNegate(false);
        rule2.setDynamicRule(dynamicRule);
        rule2.setArguments(List.of("SAVING", "DEPOSIT", ">", "1000"));
        dynamicRule.setRule(List.of(rule, rule1, rule2));

        when(dynamicRuleRepository.findAll()).thenReturn(List.of(dynamicRule));
        when(recommendationsServiceRepository.userIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.userOfCheck(any(UUID.class), eq("DEBIT"))).thenReturn(1);
        when(recommendationsServiceRepository.userOfCheck(any(UUID.class), eq("INVEST"))).thenReturn(0);
        when(recommendationsServiceRepository.userTransactionsSum(any(UUID.class), eq("SAVING"), eq("DEPOSIT"))).thenReturn(2000);

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
    public void getUserRecommendationsTest_NoRecommendations() throws Exception {
        UUID userId = UUID.randomUUID();
        DynamicRule dynamicRule = new DynamicRule();
        dynamicRule.setId(1L);
        dynamicRule.setProduct_name("Invest 500");
        dynamicRule.setProduct_id("147f6a0f-3b91-413b-ab99-87f081d60d5a");
        dynamicRule.setProduct_text(text1);
        Rule rule = new Rule();
        Rule rule1 = new Rule();
        Rule rule2 = new Rule();
        rule.setId(1L);
        rule.setQuery("USER_OF");
        rule.setNegate(false);
        rule.setDynamicRule(dynamicRule);
        rule.setArguments(List.of("DEBIT"));
        rule1.setId(2L);
        rule1.setQuery("USER_OF");
        rule1.setNegate(true);
        rule1.setDynamicRule(dynamicRule);
        rule1.setArguments(List.of("INVEST"));
        rule2.setId(3L);
        rule2.setQuery("TRANSACTION_SUM_COMPARE");
        rule2.setNegate(false);
        rule2.setDynamicRule(dynamicRule);
        rule2.setArguments(List.of("SAVING", "DEPOSIT", ">", "1000"));
        dynamicRule.setRule(List.of(rule, rule1, rule2));

        when(dynamicRuleRepository.findAll()).thenReturn(List.of(dynamicRule));
        when(recommendationsServiceRepository.userIsExist(any(UUID.class))).thenReturn(1);
        when(recommendationsServiceRepository.userOfCheck(any(UUID.class), eq("DEBIT"))).thenReturn(1);
        when(recommendationsServiceRepository.userOfCheck(any(UUID.class), eq("INVEST"))).thenReturn(0);
        when(recommendationsServiceRepository.userTransactionsSum(any(UUID.class), eq("SAVING"), eq("DEPOSIT"))).thenReturn(500);

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

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recommendation/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}