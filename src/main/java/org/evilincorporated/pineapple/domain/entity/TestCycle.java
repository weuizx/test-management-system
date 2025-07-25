package org.evilincorporated.pineapple.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.evilincorporated.pineapple.domain.enums.TestCycleState;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_cycle")
public class TestCycle {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "test_cycle_seq")
    @SequenceGenerator(
            name = "test_cycle_seq",
            sequenceName = "test_cycle_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "name",
            nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "state",
            nullable = false)
    @Enumerated(EnumType.STRING)
    private TestCycleState state;

    @Column(name = "assignee_id")
    private Long assigneeId;

    @Column(name = "project_id")
    private Long projectId;

    @ManyToOne
    @JoinColumn(name = "project_id",
            nullable = false, insertable = false, updatable = false)
    private Project project;

    @ManyToMany()
    @JoinTable(
            name = "test_case_x_cycle",
            joinColumns = @JoinColumn(name = "test_cycle_id"),
            inverseJoinColumns = @JoinColumn(name = "test_case_id")
    )
    private Set<TestCase> testCases = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "test_plan_x_cycle",
            joinColumns = @JoinColumn(name = "test_cycle_id"),
            inverseJoinColumns = @JoinColumn(name = "test_plan_id")
    )
    private Set<TestCycle> testCycles = new HashSet<>();

}
