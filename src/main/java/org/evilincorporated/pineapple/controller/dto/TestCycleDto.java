package org.evilincorporated.pineapple.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.evilincorporated.pineapple.domain.enums.TestCycleState;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCycleDto {

    private Long id;
    private String name;
    private String description;
    private TestCycleState state;
    private Long userId;
    private Long projectId;
//    private UserDto user;
//    private ProjectDto project;

}
