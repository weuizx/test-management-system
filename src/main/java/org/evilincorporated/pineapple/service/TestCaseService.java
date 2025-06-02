package org.evilincorporated.pineapple.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.TestCaseDto;
import org.evilincorporated.pineapple.controller.dto.TestCaseStepDto;
import org.evilincorporated.pineapple.domain.entity.TestCase;
import org.evilincorporated.pineapple.domain.entity.TestCaseStep;
import org.evilincorporated.pineapple.domain.mapper.TestCaseMapper;
import org.evilincorporated.pineapple.domain.mapper.TestCaseStepMapper;
import org.evilincorporated.pineapple.domain.repository.TestCaseRepository;
import org.evilincorporated.pineapple.domain.repository.TestCaseStepRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestCaseService {

    private static final String TEST_CASE_NOT_FOUND_MESSAGE = "TestCase not found by id = %s";
    private static final String TEST_CASE_STEP_NOT_FOUND_MESSAGE = "TestCaseStep not found by id = %s";

    private final TestCaseRepository testCaseRepository;
    private final TestCaseStepRepository testCaseStepRepository;
    private final TestCaseMapper testCaseMapper;
    private final TestCaseStepMapper testCaseStepMapper;

    public TestCaseDto createTestCase(TestCaseDto testCaseDto) {
        //TODO проверять что проект существует
        TestCase testCase = testCaseRepository.save(testCaseMapper.testCaseDtoToTestCase(testCaseDto));
        return testCaseMapper.testCaseToTestCaseDto(testCase);
    }

    public List<TestCaseDto> getAllOfProject(Long projectId) {
        return testCaseMapper.listTestCaseToListTestCaseDto(testCaseRepository.findAllByProjectId(projectId));
    }

    public TestCaseDto getTestCase(Long id) {
        TestCase testCase = testCaseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(TEST_CASE_NOT_FOUND_MESSAGE.formatted(id)));
        return testCaseMapper.testCaseToTestCaseDto(testCase);
    }

    public TestCaseDto updateTestCase(TestCaseDto testCaseDto) {
        //TODO проверять что проект существует если изменяется проект айди
        TestCase testCase = testCaseRepository.findById(testCaseDto.getId()).orElseThrow(
                () -> new EntityNotFoundException(TEST_CASE_NOT_FOUND_MESSAGE.formatted(testCaseDto.getId())));
        testCase = testCaseMapper.updateTestCaseFromDto(testCaseDto, testCase);
        testCaseRepository.save(testCase);
        return testCaseMapper.testCaseToTestCaseDto(testCase);
    }

    public void deleteTestCase(Long id) {
        TestCase testCase = testCaseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(TEST_CASE_NOT_FOUND_MESSAGE.formatted(id)));
        testCaseRepository.delete(testCase);
    }

    public TestCaseStepDto createTestCaseStep(TestCaseStepDto testCaseStepDto) {
        TestCaseStep testCaseStep = testCaseStepRepository.save(testCaseStepMapper.testCaseStepDtoToTestCaseStep(testCaseStepDto));
        return testCaseStepMapper.testCaseStepToTestCaseStepDto(testCaseStep);
    }

    public List<TestCaseStepDto> getAllOfTestCase(Long testCaseId) {
        return testCaseStepMapper.listTestCaseStepToListTestCaseStepDto(testCaseStepRepository.findAllByTestCaseIdOrderByIdAsc(testCaseId));
    }

    public TestCaseStepDto getTestCaseStep(Long id) {
        TestCaseStep testCaseStep = testCaseStepRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(TEST_CASE_STEP_NOT_FOUND_MESSAGE.formatted(id)));
        return testCaseStepMapper.testCaseStepToTestCaseStepDto(testCaseStep);
    }

    public TestCaseStepDto updateTestCaseStep(TestCaseStepDto testCaseStepDto) {
        TestCaseStep testCaseStep = testCaseStepRepository.findById(testCaseStepDto.getId()).orElseThrow(
                () -> new EntityNotFoundException(TEST_CASE_STEP_NOT_FOUND_MESSAGE.formatted(testCaseStepDto.getId())));
        testCaseStep = testCaseStepMapper.updateTestCaseStepFromDto(testCaseStepDto, testCaseStep);
        testCaseStepRepository.save(testCaseStep);
        return testCaseStepMapper.testCaseStepToTestCaseStepDto(testCaseStep);
    }

    public void deleteTestCaseStep(Long id) {
        TestCaseStep testCaseStep = testCaseStepRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(TEST_CASE_STEP_NOT_FOUND_MESSAGE.formatted(id)));
        testCaseStepRepository.delete(testCaseStep);
    }
}
