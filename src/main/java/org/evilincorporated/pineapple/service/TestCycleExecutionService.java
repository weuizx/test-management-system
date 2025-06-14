package org.evilincorporated.pineapple.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.TestCycleExecutionDto;
import org.evilincorporated.pineapple.domain.entity.TestCycle;
import org.evilincorporated.pineapple.domain.entity.TestCycleExecution;
import org.evilincorporated.pineapple.domain.entity.User;
import org.evilincorporated.pineapple.domain.enums.TestCycleState;
import org.evilincorporated.pineapple.domain.mapper.TestCycleExecutionMapper;
import org.evilincorporated.pineapple.domain.repository.TestCycleExecutionRepository;
import org.evilincorporated.pineapple.domain.repository.TestCycleRepository;
import org.evilincorporated.pineapple.security.service.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestCycleExecutionService {

    private static final String TEST_CYCLE_EXECUTION_NOT_FOUND_MESSAGE = "TestCycleExecution not found by id = %s";
    private static final String TEST_CYCLE_NOT_FOUND_MESSAGE = "TestCycle not found by id = %s";

    private final TestCycleExecutionRepository testCycleExecutionRepository;
    private final TestCycleRepository testCycleRepository;
    private final TestCycleExecutionMapper testCycleExecutionMapper;
    private final SecurityUtils securityUtils;

    public TestCycleExecutionDto createTestCycleExecution(TestCycleExecutionDto testCycleExecutionDto) {
        //TODO причесать
        User currentUser = securityUtils.getCurrentUser();
        TestCycle testCycle = testCycleRepository.findById(testCycleExecutionDto.getTestCycleId()).orElseThrow(
                () -> new EntityNotFoundException(TEST_CYCLE_NOT_FOUND_MESSAGE.formatted(testCycleExecutionDto.getTestCycleId())));
        testCycle.setState(getTestCycleState(testCycleExecutionDto));
        testCycleRepository.save(testCycle);
        testCycleExecutionDto.setUserId(currentUser.getId());
        testCycleExecutionDto.setExecutionDateTime(ZonedDateTime.now());
        TestCycleExecution testCycleExecution = testCycleExecutionRepository.save(testCycleExecutionMapper.testCycleExecutionDtoToTestCycleExecution(testCycleExecutionDto));
        return testCycleExecutionMapper.testCycleExecutionToTestCycleExecutionDto(testCycleExecution);
    }

    public List<TestCycleExecutionDto> getAllTestCycleExecutions() {
        return testCycleExecutionMapper.listTestCycleExecutionToListTestCycleExecutionDto(testCycleExecutionRepository.findAll());
    }

    public TestCycleExecutionDto getTestCycleExecution(Long id) {
        TestCycleExecution testCycleExecution = testCycleExecutionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(TEST_CYCLE_EXECUTION_NOT_FOUND_MESSAGE.formatted(id)));
        return testCycleExecutionMapper.testCycleExecutionToTestCycleExecutionDto(testCycleExecution);
    }

    public void deleteTestCycleExecution(Long id) {
        TestCycleExecution testCycleExecution = testCycleExecutionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(TEST_CYCLE_EXECUTION_NOT_FOUND_MESSAGE.formatted(id)));
        testCycleExecutionRepository.delete(testCycleExecution);
    }

    private TestCycleState getTestCycleState(TestCycleExecutionDto dto) {
        if (dto.getTestsFail() != 0) {
            return TestCycleState.FAILED;
        }
        if (dto.getTestsSkipped() != 0 || dto.getTestsBlocked() != 0) {
            return TestCycleState.IN_PROGRESS;
        }
        if (dto.getTestsNotExecuted() != 0) {
            if (dto.getTestsPassed() != 0) {
                return TestCycleState.IN_PROGRESS;
            } else {
                return TestCycleState.NOT_EXECUTED;
            }
        }
        return TestCycleState.PASSED;
    }
}