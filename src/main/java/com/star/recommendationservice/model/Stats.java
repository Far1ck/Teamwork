package com.star.recommendationservice.model;

import java.util.List;

// DTO для выведения статистики
public class Stats {
    private List<Counter> stats;

    public Stats(List<Counter> stats) {
        this.stats = stats;
    }

    public List<Counter> getStats() {
        return stats;
    }

    public void setStats(List<Counter> stats) {
        this.stats = stats;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "stats=" + stats +
                '}';
    }
}
