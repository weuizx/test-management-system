package org.evilincorporated.pineapple.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.evilincorporated.pineapple.domain.enums.TestCaseState;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseDto {

    private Long id;
    private String name;
    private TestCaseState state;
    private String precondition;
    private Long requirementId;
    private Long projectId;

}
