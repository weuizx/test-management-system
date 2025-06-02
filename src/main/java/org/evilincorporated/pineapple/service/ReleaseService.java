package org.evilincorporated.pineapple.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.ReleaseDto;
import org.evilincorporated.pineapple.domain.entity.Release;
import org.evilincorporated.pineapple.domain.mapper.ReleaseMapper;
import org.evilincorporated.pineapple.domain.repository.ReleaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReleaseService {

    private static final String RELEASE_NOT_FOUND_MESSAGE = "Release not found by id = %s";

    private final ReleaseRepository releaseRepository;
    private final ReleaseMapper releaseMapper;

    public ReleaseDto createRelease(ReleaseDto releaseDto) {
        Release release = releaseRepository.save(releaseMapper.releaseDtoToRelease(releaseDto));
        return releaseMapper.releaseToReleaseDto(release);
    }

    public List<ReleaseDto> getAllOfProject(Long projectId) {
        return releaseMapper.listReleaseToListReleaseDto(releaseRepository.findAllByProjectId(projectId));
    }

    public ReleaseDto getRelease(Long id) {
        Release release = releaseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(RELEASE_NOT_FOUND_MESSAGE.formatted(id)));
        return releaseMapper.releaseToReleaseDto(release);
    }

    public ReleaseDto updateRelease(ReleaseDto releaseDto) {
        Release release = releaseRepository.findById(releaseDto.getId()).orElseThrow(
                () -> new EntityNotFoundException(RELEASE_NOT_FOUND_MESSAGE.formatted(releaseDto.getId())));
        release = releaseMapper.updateReleaseFromDto(releaseDto, release);
        releaseRepository.save(release);
        return releaseMapper.releaseToReleaseDto(release);
    }

    public void deleteRelease(Long id) {
        Release release = releaseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(RELEASE_NOT_FOUND_MESSAGE.formatted(id)));
        releaseRepository.delete(release);
    }
}
