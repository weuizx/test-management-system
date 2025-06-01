package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestCaseExecutionDto;
import org.evilincorporated.pineapple.domain.entity.TestCaseExecution;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestCaseExecutionMapper {

    public abstract TestCaseExecution toEntity(TestCaseExecutionDto testCaseExecutionDto);

    public abstract TestCaseExecutionDto toDto(TestCaseExecution testCaseExecution);
}
