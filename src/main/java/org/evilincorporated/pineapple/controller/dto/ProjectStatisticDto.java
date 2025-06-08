package org.evilincorporated.pineapple.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectStatisticDto {

    private Long projectId;
    private Integer testsPassed;
    private Integer testsFailed;
    private Integer testsNotExecuted;
    private Integer testsSkipped;
    private Integer testsBlocked;
}
