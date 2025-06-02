package org.evilincorporated.pineapple.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.evilincorporated.pineapple.domain.enums.TestPlanState;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestPlanDto {

    private Long id;
    private String name;
    private String description;
    private TestPlanState state;
    private Long assigneeId;
    private Long releaseId;
    private Long specificationId;
    private List<Long> testCycleIds;

}
