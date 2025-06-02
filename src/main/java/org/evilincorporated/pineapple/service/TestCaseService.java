package org.evilincorporated.pineapple.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.TestCaseDto;
import org.evilincorporated.pineapple.domain.entity.TestCase;
import org.evilincorporated.pineapple.domain.mapper.TestCaseMapper;
import org.evilincorporated.pineapple.domain.repository.TestCaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestCaseService {

    private static final String TEST_CASE_NOT_FOUND_MESSAGE = "TestCase not found by id = %s";

    private final TestCaseRepository testCaseRepository;
    private final TestCaseMapper testCaseMapper;

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
        //TODO удалять все связи в таблице связке с циклами
        TestCase testCase = testCaseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(TEST_CASE_NOT_FOUND_MESSAGE.formatted(id)));
        testCaseRepository.delete(testCase);
    }
}
