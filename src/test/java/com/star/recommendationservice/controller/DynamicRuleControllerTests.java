package com.star.recommendationservice.controller;

import com.star.recommendationservice.model.*;
import com.star.recommendationservice.repository.DynamicRuleRepository;
import com.star.recommendationservice.service.DynamicRuleService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DynamicRuleController.class)
public class DynamicRuleControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DynamicRuleRepository dynamicRuleRepository;

    @SpyBean
    private DynamicRuleService dynamicRuleService;

    @InjectMocks
    private DynamicRuleController dynamicRuleController;

    @Test
    public void getAllRulesTest_RuleIsExist() throws Exception {
        DynamicRule dynamicRule = new DynamicRule();
        dynamicRule.setId(1L);
        dynamicRule.setProduct_name("Invest 500");
        dynamicRule.setProduct_id("147f6a0f-3b91-413b-ab99-87f081d60d5a");
        dynamicRule.setProduct_text("Text");
        Rule rule = new Rule();
        rule.setId(1L);
        rule.setQuery("USER_OF");
        rule.setNegate(false);
        rule.setDynamicRule(dynamicRule);
        rule.setArguments(List.of("DEBIT"));
        dynamicRule.setRule(List.of(rule));

        when(dynamicRuleRepository.findAll()).thenReturn(List.of(dynamicRule));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/rule")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(dynamicRule.getId()))
                .andExpect(jsonPath("$.data[0].product_name").value(dynamicRule.getProduct_name()))
                .andExpect(jsonPath("$.data[0].product_id").value(dynamicRule.getProduct_id()))
                .andExpect(jsonPath("$.data[0].product_text").value(dynamicRule.getProduct_text()))
                .andExpect(jsonPath("$.data[0].rule").isNotEmpty());
    }

    @Test
    public void getAllRulesTest_NoRulesFound() throws Exception {
        List<DynamicRule> dynamicRules = new ArrayList<>();

        when(dynamicRuleRepository.findAll()).thenReturn(dynamicRules);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/rule")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void createDynamicRuleTest_Success() throws Exception {
        DynamicRule saveResult = new DynamicRule();
        Rule rule = new Rule();
        rule.setQuery("USER_OF");
        rule.setNegate(false);
        rule.setArguments(List.of("DEBIT"));
        rule.setDynamicRule(saveResult);
        saveResult.setId(1L);
        saveResult.setProduct_name("Invest 500");
        saveResult.setProduct_id("147f6a0f-3b91-413b-ab99-87f081d60d5a");
        saveResult.setProduct_text("Text");
        saveResult.setRule(List.of(rule));
        JSONObject dynamicRuleRequest = getJsonObject();


        when(dynamicRuleRepository.save(any(DynamicRule.class))).thenReturn(saveResult);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/rule")
                        .content(dynamicRuleRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_name").value(saveResult.getProduct_name()))
                .andExpect(jsonPath("$.id").value(saveResult.getId()))
                .andExpect(jsonPath("$.product_id").value(saveResult.getProduct_id()))
                .andExpect(jsonPath("$.product_text").value(saveResult.getProduct_text()))
                .andExpect(jsonPath("$.rule").isNotEmpty());
    }

    @Test
    public void createDynamicRuleTest_FailedToAddToDatabase() throws Exception {
        JSONObject dynamicRuleRequest = getJsonObject();

        when(dynamicRuleRepository.save(any(DynamicRule.class))).thenThrow(NullPointerException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/rule")
                        .content(dynamicRuleRequest.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteDynamicRule_Success() throws Exception {
        String productId = "147f6a0f-3b91-413b-ab99-87f081d60d6a";

        doNothing().when(dynamicRuleRepository).deleteByProductId(anyString());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/rule/" + productId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getStats_ReturnSomeStats() throws Exception {
        List<Counter> counterList = List.of(new Counter(), new Counter());
        Stats result = new Stats(counterList);

        when(dynamicRuleRepository.getAllCounters()).thenReturn(counterList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/rule/stats")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stats").isNotEmpty())
                .andExpect(jsonPath("$.stats[0].count").value(0))
                .andExpect(jsonPath("$.stats[1].count").value(0));
    }

    private static JSONObject getJsonObject() throws JSONException {
        JSONObject dynamicRuleRequest = new JSONObject();
        JSONArray ruleArray = new JSONArray();
        JSONObject ruleObject = new JSONObject();
        JSONArray argumentsArray = new JSONArray();
        dynamicRuleRequest.put("product_name", "Invest 500");
        dynamicRuleRequest.put("product_id", "147f6a0f-3b91-413b-ab99-87f081d60d5a");
        dynamicRuleRequest.put("product_text", "Text");
        argumentsArray.put("DEBIT");
        ruleObject.put("query", "USER_OF");
        ruleObject.put("arguments", argumentsArray);
        ruleObject.put("negate", false);
        ruleArray.put(ruleObject);
        dynamicRuleRequest.put("rule", ruleArray);
        return dynamicRuleRequest;
    }
}
