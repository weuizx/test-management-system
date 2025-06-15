package org.evilincorporated.pineapple.controller;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.ProjectStatisticDto;
import org.evilincorporated.pineapple.controller.dto.ReleaseStatisticDto;
import org.evilincorporated.pineapple.service.StatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/project")
    public ResponseEntity<ProjectStatisticDto> projectStatistic(@RequestParam Long projectId) {
        return ResponseEntity.ok(statisticService.getProjectStatistic(projectId));
    }

    @GetMapping("/release")
    public ResponseEntity<ReleaseStatisticDto> releaseStatistic(@RequestParam Long releaseId) {
        return ResponseEntity.ok(statisticService.getReleaseStatistic(releaseId));
    }

}
