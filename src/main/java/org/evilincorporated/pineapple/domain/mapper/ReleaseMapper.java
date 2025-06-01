package org.evilincorporated.pineapple.domain.mapper;

import org.evilincorporated.pineapple.controller.dto.ReleaseDto;
import org.evilincorporated.pineapple.domain.entity.Release;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ReleaseMapper {

    public abstract Release toEntity(ReleaseDto releaseDto);

    public abstract ReleaseDto toDto(Release release);
}
