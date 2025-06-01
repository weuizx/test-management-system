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
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "project_seq")
    @SequenceGenerator(
            name = "project_seq",
            sequenceName = "project_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "name",
            nullable = false,
            unique = true)
    private String name;

}
