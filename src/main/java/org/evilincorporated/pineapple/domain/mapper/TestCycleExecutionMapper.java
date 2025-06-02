package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestCycleExecutionDto;
import org.evilincorporated.pineapple.domain.entity.TestCycleExecution;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestCycleExecutionMapper {

    public abstract TestCycleExecution toEntity(TestCycleExecutionDto testCycleExecutionDto);

    public abstract TestCycleExecutionDto toDto(TestCycleExecution testCycleExecution);
}
