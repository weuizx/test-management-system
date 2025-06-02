package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestCaseStepDto;
import org.evilincorporated.pineapple.domain.entity.TestCaseStep;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestCaseStepMapper {

    public abstract TestCaseStep testCaseStepDtoToTestCaseStep(TestCaseStepDto testCaseStepDto);

    public abstract TestCaseStepDto testCaseStepToTestCaseStepDto(TestCaseStep testCaseStep);

    public abstract List<TestCaseStepDto> listTestCaseStepToListTestCaseStepDto(List<TestCaseStep> testCaseSteps);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract TestCaseStep updateTestCaseStepFromDto(TestCaseStepDto testCaseStepDto, @MappingTarget TestCaseStep target);
}
