package org.evilincorporated.pineapple.service;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.ProjectStatisticDto;
import org.evilincorporated.pineapple.controller.dto.ReleaseStatisticDto;
import org.evilincorporated.pineapple.domain.enums.TestCaseState;
import org.evilincorporated.pineapple.domain.repository.TestCaseRepository;
import org.evilincorporated.pineapple.domain.repository.TestPlanRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

//TODO Подумать как переписать этот ужас
@Service
@RequiredArgsConstructor
public class StatisticService {

    private final TestCaseRepository testCaseRepository;
    private final TestPlanRepository testPlanRepository;

    public ProjectStatisticDto getProjectStatistic(Long projectId) {
        Map<TestCaseState, Long> testCasesByState = testCaseRepository.countTestCasesByState(projectId);

        return ProjectStatisticDto.builder()
                .projectId(projectId)
                .testsPassed(Math.toIntExact(testCasesByState.getOrDefault(TestCaseState.PASSED, 0L)))
                .testsFailed(Math.toIntExact(testCasesByState.getOrDefault(TestCaseState.FAILED, 0L)))
                .testsNotExecuted(Math.toIntExact(testCasesByState.getOrDefault(TestCaseState.NOT_EXECUTED, 0L)
                        + testCasesByState.getOrDefault(TestCaseState.IN_PROGRESS, 0L)))
                .testsSkipped(Math.toIntExact(testCasesByState.getOrDefault(TestCaseState.SKIPPED, 0L)))
                .testsBlocked(Math.toIntExact(testCasesByState.getOrDefault(TestCaseState.BLOCKED, 0L)))
                .build();
    }

    public ReleaseStatisticDto getReleaseStatistic(Long releaseId) {
        Map<TestCaseState, Long> testCasesByState = testPlanRepository.countTestCasesByStateForRelease(releaseId);

        return ReleaseStatisticDto.builder()
                .releaseId(releaseId)
                .testsPassed(Math.toIntExact(testCasesByState.getOrDefault(TestCaseState.PASSED, 0L)))
                .testsFailed(Math.toIntExact(testCasesByState.getOrDefault(TestCaseState.FAILED, 0L)))
                .testsNotExecuted(Math.toIntExact(testCasesByState.getOrDefault(TestCaseState.NOT_EXECUTED, 0L)
                        + testCasesByState.getOrDefault(TestCaseState.IN_PROGRESS, 0L)))
                .testsSkipped(Math.toIntExact(testCasesByState.getOrDefault(TestCaseState.SKIPPED, 0L)))
                .testsBlocked(Math.toIntExact(testCasesByState.getOrDefault(TestCaseState.BLOCKED, 0L)))
                .build();
    }

}
