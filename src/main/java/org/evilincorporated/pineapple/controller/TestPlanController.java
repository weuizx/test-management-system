package org.evilincorporated.pineapple.controller;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.TestPlanDto;
import org.evilincorporated.pineapple.service.TestPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test-plans")
public class TestPlanController {

    private final TestPlanService testPlanService;

    @PostMapping
    public ResponseEntity<TestPlanDto> createTestPlan(@RequestBody TestPlanDto testPlanDto) {
        return ResponseEntity.ok(testPlanService.createTestPlan(testPlanDto));
    }

    @GetMapping
    public ResponseEntity<List<TestPlanDto>> getAll() {
        return ResponseEntity.ok(testPlanService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestPlanDto> getTestPlan(@PathVariable Long id) {
        return ResponseEntity.ok(testPlanService.getTestPlan(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestPlanDto> updateTestPlan(@PathVariable Long id, TestPlanDto testPlanDto) {
        testPlanDto.setId(id);
        return ResponseEntity.ok(testPlanService.updateTestPlan(testPlanDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestPlan(@PathVariable Long id) {
        testPlanService.deleteTestPlan(id);
        return ResponseEntity.ok().build();
    }
}
