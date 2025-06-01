package org.evilincorporated.pineapple.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseDto {

    private Long id;
    private String name;
    private String description;
    private Long projectId;
//    private ProjectDto project;

}
