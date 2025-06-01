package org.evilincorporated.pineapple.domain.repository;

import org.evilincorporated.pineapple.domain.entity.TestCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCycleRepository extends JpaRepository<TestCycle, Long> {
}
