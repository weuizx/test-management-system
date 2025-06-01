package org.evilincorporated.pineapple.domain.repository;

import org.evilincorporated.pineapple.domain.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
}
