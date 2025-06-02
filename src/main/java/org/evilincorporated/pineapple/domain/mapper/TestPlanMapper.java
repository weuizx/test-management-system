package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestPlanDto;
import org.evilincorporated.pineapple.domain.entity.TestCycle;
import org.evilincorporated.pineapple.domain.entity.TestPlan;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestPlanMapper {

    @Mapping(target = "testCycles", ignore = true)
    public abstract TestPlan testPlanDtoToTestPlan(TestPlanDto testPlanDto);

    @Mapping(target = "testCycleIds", source = "testCycles", qualifiedByName = "mapTestCycles")
    public abstract TestPlanDto testPlanToTestPlanDto(TestPlan testPlan);

    @Mapping(target = "testCases", ignore = true)
    public abstract List<TestPlanDto> listTestPlanToListTestPlanDto(List<TestPlan> testPlans);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract TestPlan updateTestPlanFromDto(TestPlanDto testPlanDto, @MappingTarget TestPlan target);

    @Named("mapTestCycles")
    protected List<Long> mapTestCycles(Set<TestCycle> testCycles) {
        return testCycles.stream().map(TestCycle::getId).toList();
    }
}
