package org.evilincorporated.pineapple.controller;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.TestCaseExecutionDto;
import org.evilincorporated.pineapple.service.TestCaseExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test-case-execution")
public class TestCaseExecutionController {

    private final TestCaseExecutionService testCaseExecutionService;

    @PostMapping
    public ResponseEntity<TestCaseExecutionDto> createTestCaseExecution(@RequestBody TestCaseExecutionDto testCaseExecutionDto) {
        return ResponseEntity.ok(testCaseExecutionService.createTestCaseExecution(testCaseExecutionDto));
    }

    @GetMapping
    public ResponseEntity<List<TestCaseExecutionDto>> getAllTestCaseExecutions() {
        return ResponseEntity.ok(testCaseExecutionService.getAllTestCaseExecutions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCaseExecutionDto> getTestCaseExecution(@PathVariable Long id) {
        return ResponseEntity.ok(testCaseExecutionService.getTestCaseExecution(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCaseExecution(@PathVariable Long id) {
        testCaseExecutionService.deleteTestCaseExecution(id);
        return ResponseEntity.ok().build();
    }
}
