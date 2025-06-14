package org.evilincorporated.pineapple.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "release")
public class Release {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "release_seq")
    @SequenceGenerator(
            name = "release_seq",
            sequenceName = "release_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "name",
            nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "project_id")
    private Long projectId;

    @ManyToOne
    @JoinColumn(name = "project_id",
            nullable = false, insertable = false, updatable = false)
    private Project project;

    @OneToMany(mappedBy = "release", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TestPlan> testPlans;


}
