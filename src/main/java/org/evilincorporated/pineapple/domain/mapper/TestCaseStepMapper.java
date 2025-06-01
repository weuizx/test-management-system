package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestCaseStepDto;
import org.evilincorporated.pineapple.domain.entity.TestCaseStep;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestCaseStepMapper {

    public abstract TestCaseStep toEntity(TestCaseStepDto testCaseStepDto);

    public abstract TestCaseStepDto toDto(TestCaseStep testCaseStep);
}
