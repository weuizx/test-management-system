package org.evilincorporated.pineapple.domain.repository;

import org.evilincorporated.pineapple.domain.entity.DeactivatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeactivatedTokenRepository extends JpaRepository<DeactivatedToken, UUID> {
}
