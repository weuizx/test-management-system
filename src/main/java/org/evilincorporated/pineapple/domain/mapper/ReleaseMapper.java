package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.ReleaseDto;
import org.evilincorporated.pineapple.domain.entity.Release;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ReleaseMapper {

    public abstract Release releaseDtoToRelease(ReleaseDto releaseDto);

    public abstract ReleaseDto releaseToReleaseDto(Release release);

    public abstract List<ReleaseDto> listReleaseToListReleaseDto(List<Release> releases);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Release updateReleaseFromDto(ReleaseDto releaseDto, @MappingTarget Release target);
}
