package org.evilincorporated.pineapple.domain.repository;

import org.evilincorporated.pineapple.domain.entity.TestPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestPlanRepository extends JpaRepository<TestPlan, Long> {
}
