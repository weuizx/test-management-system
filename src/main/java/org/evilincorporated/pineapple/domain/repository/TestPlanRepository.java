package org.evilincorporated.pineapple.domain.repository;

import org.evilincorporated.pineapple.domain.entity.TestPlan;
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
public interface TestPlanRepository extends JpaRepository<TestPlan, Long> {

    @Query("SELECT tc.state, COUNT(DISTINCT tc.id) FROM TestPlan tp " +
            "JOIN tp.testCycles tcy " +
            "JOIN tcy.testCases tc " +
            "WHERE tp.releaseId = :releaseId " +
            "GROUP BY tc.state")
    List<Object[]> countTestCasesByStateForReleaseDumb(@Param("releaseId") Long releaseId);

    default Map<TestCaseState, Long> countTestCasesByStateForRelease(Long releaseId) {
        Map<TestCaseState, Long> statusCounts = new HashMap<>();
        for (Object[] row : countTestCasesByStateForReleaseDumb(releaseId)) {
            TestCaseState status = TestCaseState.valueOf(row[0].toString());
            Long count = (Long) row[1];
            statusCounts.put(status, count);
        }
        return statusCounts;
    }
}
