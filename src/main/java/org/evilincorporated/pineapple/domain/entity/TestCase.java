package org.evilincorporated.pineapple.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.evilincorporated.pineapple.domain.enums.TestCaseState;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_case")
public class TestCase {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "test_case_seq")
    @SequenceGenerator(
            name = "test_case_seq",
            sequenceName = "test_case_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "name",
            nullable = false)
    private String name;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private TestCaseState state;

    @Column(name = "precondition")
    private String precondition;

    @Column(name = "requirement_id")
    private Long requirementId;

    @Column(name = "project_id")
    private Long projectId;

    @ManyToMany
    @JoinTable(
            name = "test_case_x_cycle",
            joinColumns = @JoinColumn(name = "test_case_id"),
            inverseJoinColumns = @JoinColumn(name = "test_cycle_id")
    )
    private Set<TestCycle> testCycles = new HashSet<>();

}
