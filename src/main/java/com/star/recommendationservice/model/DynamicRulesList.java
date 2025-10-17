package com.star.recommendationservice.model;

import java.util.List;

public class DynamicRulesList {
    private List<DynamicRule> data;

    public DynamicRulesList(List<DynamicRule> data) {
        this.data = data;
    }

    public List<DynamicRule> getData() {
        return data;
    }

    public void setData(List<DynamicRule> data) {
        this.data = data;
    }
}
