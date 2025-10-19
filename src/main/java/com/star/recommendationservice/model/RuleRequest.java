package com.star.recommendationservice.model;

import java.util.List;

// DTO для получения JSON из запроса и преобразования его в список Rule
public class RuleRequest {
    private String query;
    private List<String> arguments;
    private boolean negate;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }
}
