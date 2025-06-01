package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestPlanDto;
import org.evilincorporated.pineapple.domain.entity.TestPlan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestPlanMapper {

    public abstract TestPlan toEntity(TestPlanDto testPlanDto);

    public abstract TestPlanDto toDto(TestPlan testPlan);
}
