package org.evilincorporated.pineapple.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_case_step")
public class TestCaseStep {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "test_case_step_seq")
    @SequenceGenerator(
            name = "test_case_step_seq",
            sequenceName = "test_case_step_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "test_data")
    private String testData;

    @Column(name = "description")
    private String description;

    @Column(name = "expected_result")
    private String expectedResult;

    @Column(name = "test_case_id")
    private Long testCaseId;

}