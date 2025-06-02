package org.evilincorporated.pineapple.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCycleExecutionDto {

    private Long id;
    private ZonedDateTime executionDateTime;
    private Integer testsPassed;
    private Integer testsFail;
    private Integer testsNotExecuted;
    private Integer testsSkipped;
    private Integer testsBlocked;
    private UserDto user;
    private Long testCycleId;
//    private TestCycleDto testCycle;

}