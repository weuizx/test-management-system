package org.evilincorporated.pineapple.domain.repository;

import org.evilincorporated.pineapple.domain.entity.ClientAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientAuthorityRepository extends JpaRepository<ClientAuthority, Long> {
}
