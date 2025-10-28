package com.star.recommendationservice.service;
import com.star.recommendationservice.repository.RecommendationsServiceRepository;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

@Service
public class ManagementService {

    private final RecommendationsServiceRepository recommendationsServiceRepository;

    private final BuildProperties buildProperties;

    public ManagementService(RecommendationsServiceRepository recommendationsServiceRepository, BuildProperties buildProperties) {
        this.recommendationsServiceRepository = recommendationsServiceRepository;
        this.buildProperties = buildProperties;
    }

    public void clearCaches() {
        recommendationsServiceRepository.clearAllCaches();
    }

    public String getVersion() {
        return buildProperties.getVersion();
    }

    public String getName() {
        return buildProperties.getName();
    }
}
