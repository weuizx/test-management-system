package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestCycleDto;
import org.evilincorporated.pineapple.domain.entity.TestCycle;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestCycleMapper {

    public abstract TestCycle toEntity(TestCycleDto testCycleDto);

    public abstract TestCycleDto toDto(TestCycle testCycle);
}
