package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestCycleDto;
import org.evilincorporated.pineapple.domain.entity.TestCase;
import org.evilincorporated.pineapple.domain.entity.TestCycle;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestCycleMapper {

    @Mapping(target = "testCases", ignore = true)
    public abstract TestCycle testCycleDtoToTestCycle(TestCycleDto testCycleDto);

    @Mapping(target = "testCaseIds", source = "testCases", qualifiedByName = "mapTestCases")
    public abstract TestCycleDto testCycleToTestCycleDto(TestCycle testCycle);

    public abstract List<TestCycleDto> listTestCycleToListTestCycleDto(List<TestCycle> testCycles);

    @Mapping(target = "testCases", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract TestCycle updateTestCycleFromDto(TestCycleDto testCycleDto, @MappingTarget TestCycle target);

    @Named("mapTestCases")
    protected List<Long> mapTestCases(Set<TestCase> testCases) {
        return testCases.stream().map(TestCase::getId).toList();
    }
}
