package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.ProjectDto;
import org.evilincorporated.pineapple.domain.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ProjectMapper {

    public abstract Project toEntity(ProjectDto projectDto);

    public abstract ProjectDto toDto(Project project);
}
