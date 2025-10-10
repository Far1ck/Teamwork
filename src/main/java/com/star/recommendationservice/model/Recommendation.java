package com.star.recommendationservice.model;

import java.util.UUID;

public class Recommendation {
    private String name;
    private UUID id;
    private String text;

    public Recommendation() {}

    public Recommendation(String name, UUID id, String text) {
        this.name = name;
        this.id = id;
        this.text = text;
    }
}
