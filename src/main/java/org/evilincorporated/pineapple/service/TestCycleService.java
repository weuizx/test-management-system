package org.evilincorporated.pineapple.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.TestCycleDto;
import org.evilincorporated.pineapple.domain.entity.TestCase;
import org.evilincorporated.pineapple.domain.entity.TestCycle;
import org.evilincorporated.pineapple.domain.mapper.TestCycleMapper;
import org.evilincorporated.pineapple.domain.repository.TestCaseRepository;
import org.evilincorporated.pineapple.domain.repository.TestCycleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TestCycleService {

    private static final String TEST_CYCLE_NOT_FOUND_MESSAGE = "TestCycle not found by id = %s";

    private final TestCycleRepository testCycleRepository;
    private final TestCaseRepository testCaseRepository;
    private final TestCycleMapper testCycleMapper;

    public TestCycleDto createTestCycle(TestCycleDto testCycleDto) {
        //TODO проверять что проект существует
        //TODO проверять что юзер существует
        TestCycle testCycle = testCycleMapper.testCycleDtoToTestCycle(testCycleDto);
        if (testCycleDto.getTestCaseIds() != null) {
            Set<TestCase> testCases = new HashSet<>(testCaseRepository.findAllById(testCycleDto.getTestCaseIds()));
            testCycle.setTestCases(testCases);
        }
        testCycle = testCycleRepository.save(testCycle);
        return testCycleMapper.testCycleToTestCycleDto(testCycle);
    }

    public List<TestCycleDto> getAllOfProject(Long projectId) {
        return testCycleMapper.listTestCycleToListTestCycleDto(testCycleRepository.findAllByProjectId(projectId));
    }

    public TestCycleDto getTestCycle(Long id) {
        TestCycle testCycle = testCycleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(TEST_CYCLE_NOT_FOUND_MESSAGE.formatted(id)));
        return testCycleMapper.testCycleToTestCycleDto(testCycle);
    }

    public TestCycleDto updateTestCycle(TestCycleDto testCycleDto) {
        //TODO проверять что проект существует если изменяется проект айди
        //TODO проверять что юзер существует если изменяется юзер
        TestCycle testCycle = testCycleRepository.findById(testCycleDto.getId()).orElseThrow(
                () -> new EntityNotFoundException(TEST_CYCLE_NOT_FOUND_MESSAGE.formatted(testCycleDto.getId())));
        testCycle = testCycleMapper.updateTestCycleFromDto(testCycleDto, testCycle);
        if (testCycleDto.getTestCaseIds() != null) {
            Set<TestCase> testCases = new HashSet<>(testCaseRepository.findAllById(testCycleDto.getTestCaseIds()));
            testCycle.setTestCases(testCases);
        }
        testCycleRepository.save(testCycle);
        return testCycleMapper.testCycleToTestCycleDto(testCycle);
    }

    public void deleteTestCycle(Long id) {
        TestCycle testCycle = testCycleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(TEST_CYCLE_NOT_FOUND_MESSAGE.formatted(id)));
        testCycleRepository.delete(testCycle);
    }
}
