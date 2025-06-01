package org.evilincorporated.pineapple.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_cycle_execution_history")
public class TestCycleExecution {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "test_cycle_execution_history_seq")
    @SequenceGenerator(
            name = "test_cycle_execution_history_seq",
            sequenceName = "test_cycle_execution_history_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "execution_date_time")
    private ZonedDateTime executionDateTime;

    @Column(name = "tests_passed")
    private Integer testsPassed;

    @Column(name = "tests_fail")
    private Integer testsFail;

    @Column(name = "tests_not_executed")
    private Integer testsNotExecuted;

    @Column(name = "tests_skipped")
    private Integer testsSkipped;

    @Column(name = "tests_blocked")
    private Integer testsBlocked;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "test_cycle_id")
    private Long testCycleId;

}