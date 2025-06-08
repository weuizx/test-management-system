package org.evilincorporated.pineapple.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.TestCaseExecutionDto;
import org.evilincorporated.pineapple.domain.entity.TestCaseExecution;
import org.evilincorporated.pineapple.domain.entity.User;
import org.evilincorporated.pineapple.domain.mapper.TestCaseExecutionMapper;
import org.evilincorporated.pineapple.domain.repository.TestCaseExecutionRepository;
import org.evilincorporated.pineapple.security.service.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestCaseExecutionService {

    private static final String PROJECT_NOT_FOUND_MESSAGE = "TestCaseExecution not found by id = %s";

    private final TestCaseExecutionRepository testCaseExecutionRepository;
    private final TestCaseExecutionMapper testCaseExecutionMapper;
    private final SecurityUtils securityUtils;

    public TestCaseExecutionDto createTestCaseExecution(TestCaseExecutionDto testCaseExecutionDto) {
        //TODO проверять на существование тест кейс
        User currentUser = securityUtils.getCurrentUser();
        testCaseExecutionDto.setUserId(currentUser.getId());
        testCaseExecutionDto.setExecutionDateTime(ZonedDateTime.now());
        TestCaseExecution testCaseExecution = testCaseExecutionRepository.save(testCaseExecutionMapper.testCaseExecutionDtoToTestCaseExecution(testCaseExecutionDto));
        return testCaseExecutionMapper.testCaseExecutionToTestCaseExecutionDto(testCaseExecution);
    }

    public List<TestCaseExecutionDto> getAllTestCaseExecutions() {
        return testCaseExecutionMapper.listTestCaseExecutionToListTestCaseExecutionDto(testCaseExecutionRepository.findAll());
    }

    public TestCaseExecutionDto getTestCaseExecution(Long id) {
        TestCaseExecution testCaseExecution = testCaseExecutionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(PROJECT_NOT_FOUND_MESSAGE.formatted(id)));
        return testCaseExecutionMapper.testCaseExecutionToTestCaseExecutionDto(testCaseExecution);
    }

    public void deleteTestCaseExecution(Long id) {
        TestCaseExecution testCaseExecution = testCaseExecutionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(PROJECT_NOT_FOUND_MESSAGE.formatted(id)));
        testCaseExecutionRepository.delete(testCaseExecution);
    }
}

