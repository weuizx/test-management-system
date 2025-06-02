package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestCycleDto;
import org.evilincorporated.pineapple.domain.entity.TestCycle;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestCycleMapper {

    public abstract TestCycle testCycleDtoToTestCycle(TestCycleDto testCycleDto);

    public abstract TestCycleDto testCycleToTestCycleDto(TestCycle testCycle);

    public abstract List<TestCycleDto> listTestCycleToListTestCycleDto(List<TestCycle> testCycles);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract TestCycle updateTestCycleFromDto(TestCycleDto testCycleDto, @MappingTarget TestCycle target);
}
