package org.evilincorporated.pineapple.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.TestCycleExecutionDto;
import org.evilincorporated.pineapple.domain.entity.TestCycleExecution;
import org.evilincorporated.pineapple.domain.entity.User;
import org.evilincorporated.pineapple.domain.mapper.TestCycleExecutionMapper;
import org.evilincorporated.pineapple.domain.repository.TestCycleExecutionRepository;
import org.evilincorporated.pineapple.security.service.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestCycleExecutionService {

    private static final String PROJECT_NOT_FOUND_MESSAGE = "TestCycleExecution not found by id = %s";

    private final TestCycleExecutionRepository testCycleExecutionRepository;
    private final TestCycleExecutionMapper testCycleExecutionMapper;
    private final SecurityUtils securityUtils;

    public TestCycleExecutionDto createTestCycleExecution(TestCycleExecutionDto testCycleExecutionDto) {
        //TODO проверять на существование тест кейс
        User currentUser = securityUtils.getCurrentUser();
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
                () -> new EntityNotFoundException(PROJECT_NOT_FOUND_MESSAGE.formatted(id)));
        return testCycleExecutionMapper.testCycleExecutionToTestCycleExecutionDto(testCycleExecution);
    }

    public void deleteTestCycleExecution(Long id) {
        TestCycleExecution testCycleExecution = testCycleExecutionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(PROJECT_NOT_FOUND_MESSAGE.formatted(id)));
        testCycleExecutionRepository.delete(testCycleExecution);
    }
}