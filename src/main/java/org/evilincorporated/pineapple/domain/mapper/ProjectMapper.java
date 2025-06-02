package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.ProjectDto;
import org.evilincorporated.pineapple.domain.entity.Project;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ProjectMapper {

    public abstract Project projectDtoToProject(ProjectDto projectDto);

    public abstract ProjectDto projectToProjectDto(Project project);

    public abstract List<ProjectDto> listProjectToListProjectDto(List<Project> projects);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Project updateProjectFromDto(ProjectDto projectDto, @MappingTarget Project target);
}
