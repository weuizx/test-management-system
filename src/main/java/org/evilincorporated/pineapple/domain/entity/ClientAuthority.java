package org.evilincorporated.pineapple.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.evilincorporated.pineapple.security.service.UserRole;

@Entity
@Table(name = "client_authority")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientAuthority {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_authority_seq"
    )
    @SequenceGenerator(
            name = "client_authority_seq",
            sequenceName = "client_authority_id_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
