package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.TestPlanDto;
import org.evilincorporated.pineapple.domain.entity.TestPlan;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TestPlanMapper {

    public abstract TestPlan testPlanDtoToTestPlan(TestPlanDto testPlanDto);

    public abstract TestPlanDto testPlanToTestPlanDto(TestPlan testPlan);

    public abstract List<TestPlanDto> listTestPlanToListTestPlanDto(List<TestPlan> testPlans);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract TestPlan updateTestPlanFromDto(TestPlanDto testPlanDto, @MappingTarget TestPlan target);
}
