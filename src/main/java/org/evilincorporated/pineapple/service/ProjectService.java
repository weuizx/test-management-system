package org.evilincorporated.pineapple.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.ProjectDto;
import org.evilincorporated.pineapple.domain.entity.Project;
import org.evilincorporated.pineapple.domain.mapper.ProjectMapper;
import org.evilincorporated.pineapple.domain.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private static final String PROJECT_NOT_FOUND_MESSAGE = "Project not found by id = %s";

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectDto createProject(ProjectDto projectDto) {
        //TODO имя проверять на существующее
        Project project = projectRepository.save(projectMapper.projectDtoToProject(projectDto));
        return projectMapper.projectToProjectDto(project);
    }

    public List<ProjectDto> getAllProjects() {
        return projectMapper.listProjectToListProjectDto(projectRepository.findAll());
    }

    public ProjectDto getProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(PROJECT_NOT_FOUND_MESSAGE.formatted(id)));
        return projectMapper.projectToProjectDto(project);
    }

    public ProjectDto updateProject(ProjectDto projectDto) {
        //TODO имя проверять на существующее
        Project project = projectRepository.findById(projectDto.getId()).orElseThrow(
                () -> new EntityNotFoundException(PROJECT_NOT_FOUND_MESSAGE.formatted(projectDto.getId())));
        project = projectMapper.updateProjectFromDto(projectDto, project);
        projectRepository.save(project);
        return projectMapper.projectToProjectDto(project);
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(PROJECT_NOT_FOUND_MESSAGE.formatted(id)));
        projectRepository.delete(project);
    }
}
