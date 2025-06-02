package org.evilincorporated.pineapple.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deactivated_token")
public class DeactivatedToken {

    @Id
    private UUID id;

    @Column(name = "keep_until",
            nullable = false)
    private Instant keepUntil;
}
