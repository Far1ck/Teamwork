package com.star.recommendationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "response_counters")
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String rule_id;

    @Column(name = "counter")
    private int count;

    public Counter() {}

    public Counter(String rule_id, int count) {
        this.rule_id = rule_id;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRule_id() {
        return rule_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Counter counter = (Counter) o;
        return count == counter.count && Objects.equals(rule_id, counter.rule_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule_id, count);
    }

    @Override
    public String toString() {
        return "Counter{" +
                "rule_id='" + rule_id + '\'' +
                ", count=" + count +
                '}';
    }
}
