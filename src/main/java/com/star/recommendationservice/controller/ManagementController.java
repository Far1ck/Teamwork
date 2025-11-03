package com.star.recommendationservice.controller;

import com.star.recommendationservice.model.BuildInfo;
import com.star.recommendationservice.service.ManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
public class ManagementController {
    private final ManagementService managementService;

    public ManagementController(ManagementService managementService) {
        this.managementService = managementService;
    }

    // Метод очистки кэша
    @PostMapping("/clear-caches")
    public void clearCaches() {
        managementService.clearCaches();
    }

    // Метод для получения информации о сборке
    @GetMapping("/info")
    ResponseEntity<BuildInfo> getInfo() {
        BuildInfo buildInfo = new BuildInfo(managementService.getName(), managementService.getVersion());
        return ResponseEntity.ok(buildInfo);
    }
}
