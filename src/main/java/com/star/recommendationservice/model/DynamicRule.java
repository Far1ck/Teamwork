package com.star.recommendationservice.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dynamic_rules")
public class DynamicRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String product_name;
    private String product_id;
    private String product_text;

    @OneToMany(mappedBy = "dynamicRule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rule> rule = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_text() {
        return product_text;
    }

    public void setProduct_text(String product_text) {
        this.product_text = product_text;
    }

    public List<Rule> getRule() {
        return rule;
    }

    public void setRule(List<Rule> rule) {
        this.rule = rule;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DynamicRule dynamicRule = (DynamicRule) o;
        return Objects.equals(id, dynamicRule.id) && Objects.equals(product_name, dynamicRule.product_name) && Objects.equals(product_id, dynamicRule.product_id) && Objects.equals(product_text, dynamicRule.product_text) && Objects.equals(rule, dynamicRule.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product_name, product_id, product_text, rule);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", product_name='" + product_name + '\'' +
                ", product_id='" + product_id + '\'' +
                ", product_text='" + product_text + '\'' +
                ", rule=" + rule +
                '}';
    }
}
