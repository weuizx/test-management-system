package org.evilincorporated.pineapple.domain.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "test_case_execution_history")
public class TestCaseExecution {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "test_case_execution_history_seq")
    @SequenceGenerator(
            name = "test_case_execution_history_seq",
            sequenceName = "test_case_execution_history_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "execution_date_time")
    private ZonedDateTime executionDateTime;

    @Column(name = "result")
    private String result;

    @Column(name = "state",
            nullable = false)
    @Enumerated(EnumType.STRING)
    private TestCaseState state;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "test_case_id")
    private Long testCaseId;

}
