package com.star.recommendationservice.model;

import org.springframework.beans.factory.annotation.Value;

public class BuildInfo {

    private String name;
    private String version;

    public BuildInfo(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
