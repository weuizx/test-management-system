package org.evilincorporated.pineapple.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.evilincorporated.pineapple.security.service.UserRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_authority")
public class UserAuthority {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_authority_seq"
    )
    @SequenceGenerator(
            name = "user_authority_seq",
            sequenceName = "user_authority_id_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",
            nullable = false)
    private User user;

    @Column(name = "role",
            nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
