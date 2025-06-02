package org.evilincorporated.pineapple.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.evilincorporated.pineapple.domain.enums.TestCaseExecutionState;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseExecutionDto {

    private Long id;
    private ZonedDateTime executionDateTime;
    private String result;
    private TestCaseExecutionState state;
    private UserDto user;
    private Long testCaseId;
//    private TestCaseDto testCase;

}
