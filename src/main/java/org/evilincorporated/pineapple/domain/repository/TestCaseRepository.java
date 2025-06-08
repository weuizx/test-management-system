package org.evilincorporated.pineapple.domain.repository;

import org.evilincorporated.pineapple.domain.entity.TestCase;
import org.evilincorporated.pineapple.domain.enums.TestCaseState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO Подумать как переписать красивее эти методы
@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    List<TestCase> findAllByProjectId(Long projectId);

    @Query("SELECT t.state, COUNT(DISTINCT t.id) FROM TestCase t WHERE t.projectId = :projectId GROUP BY t.state")
    List<Object[]> countTestCasesByStateDumb(@Param("projectId") Long projectId);

    default Map<TestCaseState, Long> countTestCasesByState(Long projectId) {
        Map<TestCaseState, Long> statusCounts = new HashMap<>();
        for (Object[] row : countTestCasesByStateDumb(projectId)) {
            TestCaseState status = TestCaseState.valueOf(row[0].toString());
            Long count = (Long) row[1];
            statusCounts.put(status, count);
        }
        return statusCounts;
    }

}
