package org.evilincorporated.pineapple.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.TestCaseExecutionDto;
import org.evilincorporated.pineapple.domain.entity.TestCase;
import org.evilincorporated.pineapple.domain.entity.TestCaseExecution;
import org.evilincorporated.pineapple.domain.entity.User;
import org.evilincorporated.pineapple.domain.mapper.TestCaseExecutionMapper;
import org.evilincorporated.pineapple.domain.repository.TestCaseExecutionRepository;
import org.evilincorporated.pineapple.domain.repository.TestCaseRepository;
import org.evilincorporated.pineapple.security.service.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestCaseExecutionService {

    private static final String TEST_CASE_EXECUTION_NOT_FOUND_MESSAGE = "TestCaseExecution not found by id = %s";
    private static final String TEST_CASE_NOT_FOUND_MESSAGE = "TestCase not found by id = %s";

    private final TestCaseExecutionRepository testCaseExecutionRepository;
    private final TestCaseRepository testCaseRepository;
    private final TestCaseExecutionMapper testCaseExecutionMapper;
    private final SecurityUtils securityUtils;

    public TestCaseExecutionDto createTestCaseExecution(TestCaseExecutionDto testCaseExecutionDto) {
        //TODO причесать
        User currentUser = securityUtils.getCurrentUser();
        TestCase testCase = testCaseRepository.findById(testCaseExecutionDto.getTestCaseId()).orElseThrow(
                () -> new EntityNotFoundException(TEST_CASE_NOT_FOUND_MESSAGE.formatted(testCaseExecutionDto.getTestCaseId())));
        testCase.setState(testCaseExecutionDto.getState());
        testCaseRepository.save(testCase);
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
                () -> new EntityNotFoundException(TEST_CASE_EXECUTION_NOT_FOUND_MESSAGE.formatted(id)));
        return testCaseExecutionMapper.testCaseExecutionToTestCaseExecutionDto(testCaseExecution);
    }

    public void deleteTestCaseExecution(Long id) {
        TestCaseExecution testCaseExecution = testCaseExecutionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(TEST_CASE_EXECUTION_NOT_FOUND_MESSAGE.formatted(id)));
        testCaseExecutionRepository.delete(testCaseExecution);
    }

}

