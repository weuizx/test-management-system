package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestCycleExecutionDto;
import org.evilincorporated.pineapple.domain.entity.TestCycleExecution;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestCycleExecutionMapper {

    public abstract TestCycleExecution testCycleExecutionDtoToTestCycleExecution(TestCycleExecutionDto testCycleExecutionDto);

    public abstract TestCycleExecutionDto testCycleExecutionToTestCycleExecutionDto(TestCycleExecution testCycleExecution);

    public abstract List<TestCycleExecutionDto> listTestCycleExecutionToListTestCycleExecutionDto(List<TestCycleExecution> testCycleExecutionList);
}
