package org.evilincorporated.pineapple.domain.repository;

import org.evilincorporated.pineapple.domain.entity.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long> {
}
