package org.evilincorporated.pineapple.controller;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.TestCycleExecutionDto;
import org.evilincorporated.pineapple.service.TestCycleExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test-cycle-execution")
public class TestCycleExecutionController {

    private final TestCycleExecutionService testCycleExecutionService;

    @PostMapping
    public ResponseEntity<TestCycleExecutionDto> createTestCycleExecution(@RequestBody TestCycleExecutionDto testCycleExecutionDto) {
        return ResponseEntity.ok(testCycleExecutionService.createTestCycleExecution(testCycleExecutionDto));
    }

    @GetMapping
    public ResponseEntity<List<TestCycleExecutionDto>> getAllTestCycleExecutions() {
        return ResponseEntity.ok(testCycleExecutionService.getAllTestCycleExecutions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCycleExecutionDto> getTestCycleExecution(@PathVariable Long id) {
        return ResponseEntity.ok(testCycleExecutionService.getTestCycleExecution(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCycleExecution(@PathVariable Long id) {
        testCycleExecutionService.deleteTestCycleExecution(id);
        return ResponseEntity.ok().build();
    }
}
