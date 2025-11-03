package com.star.recommendationservice.service;
import com.star.recommendationservice.repository.RecommendationsServiceRepository;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

/**
 * Сервис для очистки кэша и получения информации о сборке
 */
@Service
public class ManagementService {

    private final RecommendationsServiceRepository recommendationsServiceRepository;

    private final BuildProperties buildProperties;

    public ManagementService(RecommendationsServiceRepository recommendationsServiceRepository, BuildProperties buildProperties) {
        this.recommendationsServiceRepository = recommendationsServiceRepository;
        this.buildProperties = buildProperties;
    }

    // Метод для очистки кэша
    public void clearCaches() {
        recommendationsServiceRepository.clearAllCaches();
    }

    // Метод для получения версии сборки
    public String getVersion() {
        return buildProperties.getVersion();
    }

    // Метод для получения имени сборки
    public String getName() {
        return buildProperties.getName();
    }
}
