package org.evilincorporated.pineapple.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.evilincorporated.pineapple.domain.enums.TestPlanState;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_plan")
public class TestPlan {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "test_plan_seq")
    @SequenceGenerator(
            name = "test_plan_seq",
            sequenceName = "test_plan_id_seq",
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
    private TestPlanState state;

    @Column(name = "specification_id")
    private Long specificationId;

    @Column(name = "assignee_id")
    private Long assigneeId;

    @Column(name = "release_id")
    private Long releaseId;

    @ManyToOne
    @JoinColumn(name = "release_id",
            nullable = false, insertable = false, updatable = false)
    private Release release;

    @ManyToMany
    @JoinTable(
            name = "test_plan_x_cycle",
            joinColumns = @JoinColumn(name = "test_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "test_cycle_id")
    )
    private Set<TestCycle> testCycles = new HashSet<>();

}
