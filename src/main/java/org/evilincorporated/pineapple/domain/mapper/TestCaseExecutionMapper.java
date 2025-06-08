package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestCaseExecutionDto;
import org.evilincorporated.pineapple.domain.entity.TestCaseExecution;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestCaseExecutionMapper {

    public abstract TestCaseExecution testCaseExecutionDtoToTestCaseExecution(TestCaseExecutionDto testCaseExecutionDto);

    public abstract TestCaseExecutionDto testCaseExecutionToTestCaseExecutionDto(TestCaseExecution testCaseExecution);

    public abstract List<TestCaseExecutionDto> listTestCaseExecutionToListTestCaseExecutionDto(List<TestCaseExecution> testCaseExecutionList);
}
