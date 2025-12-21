package com.anuj.algotracker.controller;

import com.anuj.algotracker.dto.DashboardSummary;
import com.anuj.algotracker.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // GET /api/dashboard/summary
    @GetMapping("/summary")
    public DashboardSummary getSummary() {
        return dashboardService.getSummaryForCurrentUser();
    }
}
