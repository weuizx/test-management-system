package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestCaseDto;
import org.evilincorporated.pineapple.domain.entity.TestCase;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestCaseMapper {

    public abstract TestCase toEntity(TestCaseDto testCaseDto);

    public abstract TestCaseDto toDto(TestCase testCase);
}
