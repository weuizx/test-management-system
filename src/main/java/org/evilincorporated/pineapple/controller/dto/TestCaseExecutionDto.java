package org.evilincorporated.pineapple.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.evilincorporated.pineapple.domain.enums.TestCaseState;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseExecutionDto {

    private Long id;
    private ZonedDateTime executionDateTime;
    private String result;
    private TestCaseState state;
    private Long userId;
    private Long testCaseId;

}
