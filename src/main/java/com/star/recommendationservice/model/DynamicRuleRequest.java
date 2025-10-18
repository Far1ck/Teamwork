package com.star.recommendationservice.model;

import java.util.List;

// DTO для получения JSON из запроса и последующего преобразования его в DynamicRule
public class DynamicRuleRequest {
    private String productName;
    private String productId;
    private String productText;
    private List<RuleRequest> rules;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<RuleRequest> getRules() {
        return rules;
    }

    public void setRules(List<RuleRequest> rules) {
        this.rules = rules;
    }
}
