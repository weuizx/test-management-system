package org.evilincorporated.pineapple.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.TestPlanDto;
import org.evilincorporated.pineapple.domain.entity.TestPlan;
import org.evilincorporated.pineapple.domain.mapper.TestPlanMapper;
import org.evilincorporated.pineapple.domain.repository.TestPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestPlanService {

    private static final String TEST_PLAN_NOT_FOUND_MESSAGE = "TestPlan not found by id = %s";

    private final TestPlanRepository testPlanRepository;
    private final TestPlanMapper testPlanMapper;

    public TestPlanDto createTestPlan(TestPlanDto testPlanDto) {
        //TODO проверять что релиз существует
        //TODO проверять что юзер существует
        TestPlan testPlan = testPlanRepository.save(testPlanMapper.testPlanDtoToTestPlan(testPlanDto));
        return testPlanMapper.testPlanToTestPlanDto(testPlan);
    }

    public List<TestPlanDto> getAll() {
        return testPlanMapper.listTestPlanToListTestPlanDto(testPlanRepository.findAll());
    }

    public TestPlanDto getTestPlan(Long id) {
        TestPlan testPlan = testPlanRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(TEST_PLAN_NOT_FOUND_MESSAGE.formatted(id)));
        return testPlanMapper.testPlanToTestPlanDto(testPlan);
    }

    public TestPlanDto updateTestPlan(TestPlanDto testPlanDto) {
        //TODO проверять что релиз существует если изменяется релиз айди
        //TODO проверять что юзер существует если изменяется юзер
        TestPlan testPlan = testPlanRepository.findById(testPlanDto.getId()).orElseThrow(
                () -> new EntityNotFoundException(TEST_PLAN_NOT_FOUND_MESSAGE.formatted(testPlanDto.getId())));
        testPlan = testPlanMapper.updateTestPlanFromDto(testPlanDto, testPlan);
        testPlanRepository.save(testPlan);
        return testPlanMapper.testPlanToTestPlanDto(testPlan);
    }

    public void deleteTestPlan(Long id) {
        //TODO удалять все связи в таблице связке с циклами
        TestPlan testPlan = testPlanRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(TEST_PLAN_NOT_FOUND_MESSAGE.formatted(id)));
        testPlanRepository.delete(testPlan);
    }
}
