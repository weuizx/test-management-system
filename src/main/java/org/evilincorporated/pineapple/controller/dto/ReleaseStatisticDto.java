package org.evilincorporated.pineapple.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReleaseStatisticDto {

    private Long releaseId;
    private Integer testsPassed;
    private Integer testsFailed;
    private Integer testsNotExecuted;
    private Integer testsSkipped;
    private Integer testsBlocked;
}