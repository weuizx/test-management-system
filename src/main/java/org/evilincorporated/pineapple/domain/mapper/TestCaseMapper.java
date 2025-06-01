package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestCaseDto;
import org.evilincorporated.pineapple.domain.entity.TestCase;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestCaseMapper {

    public abstract TestCase testCaseDtoToTestCase(TestCaseDto testCaseDto);

    public abstract TestCaseDto testCaseToTestCaseDto(TestCase testCase);

    public abstract List<TestCaseDto> listTestCaseToListTestCaseDto(List<TestCase> testCases);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract TestCase updateTestCaseFromDto(TestCaseDto testCaseDto, @MappingTarget TestCase target);
}
