package org.evilincorporated.pineapple.domain.repository;

import org.evilincorporated.pineapple.domain.entity.TestCaseStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseStepRepository extends JpaRepository<TestCaseStep, Long> {

    List<TestCaseStep> findAllByTestCaseIdOrderByIdAsc(Long testCaseId);
}
