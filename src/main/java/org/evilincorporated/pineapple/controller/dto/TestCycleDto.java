package org.evilincorporated.pineapple.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.evilincorporated.pineapple.domain.enums.TestCycleState;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCycleDto {

    private Long id;
    private String name;
    private String description;
    private TestCycleState state;
    private Long assigneeId;
    private Long projectId;
    private List<Long> testCaseIds;

}
