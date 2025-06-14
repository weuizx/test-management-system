package org.evilincorporated.pineapple.controller;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.TestCaseDto;
import org.evilincorporated.pineapple.controller.dto.TestCaseStepDto;
import org.evilincorporated.pineapple.service.TestCaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test-cases")
public class TestCaseController {

    private final TestCaseService testCaseService;

    @PostMapping
    public ResponseEntity<TestCaseDto> createTestCase(@RequestBody TestCaseDto testCaseDto) {
        return ResponseEntity.ok(testCaseService.createTestCase(testCaseDto));
    }

    @GetMapping
    public ResponseEntity<List<TestCaseDto>> getAllOfProject(@RequestParam Long projectId) {
        return ResponseEntity.ok(testCaseService.getAllOfProject(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCaseDto> getTestCase(@PathVariable Long id) {
        return ResponseEntity.ok(testCaseService.getTestCase(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestCaseDto> updateTestCase(@PathVariable Long id, @RequestBody TestCaseDto testCaseDto) {
        testCaseDto.setId(id);
        return ResponseEntity.ok(testCaseService.updateTestCase(testCaseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCase(@PathVariable Long id) {
        testCaseService.deleteTestCase(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/test-case-steps")
    public ResponseEntity<TestCaseStepDto> createTestCaseStep(@PathVariable Long id, @RequestBody TestCaseStepDto testCaseStepDto) {
        testCaseStepDto.setTestCaseId(id);
        return ResponseEntity.ok(testCaseService.createTestCaseStep(testCaseStepDto));
    }

    @GetMapping("/{id}/test-case-steps")
    public ResponseEntity<List<TestCaseStepDto>> getAllOfTestCase(@PathVariable Long id) {
        return ResponseEntity.ok(testCaseService.getAllOfTestCase(id));
    }

    @PutMapping("/{id}/test-case-steps")
    public ResponseEntity<TestCaseStepDto> updateTestCaseStep(@PathVariable Long id, @RequestBody TestCaseStepDto testCaseStepDto) {
        testCaseStepDto.setTestCaseId(id);
        return ResponseEntity.ok(testCaseService.updateTestCaseStep(testCaseStepDto));
    }

    @DeleteMapping("/{id}/test-case-steps")
    public ResponseEntity<Void> deleteTestCaseStep(@PathVariable Long id, @RequestParam Long testCaseId) {
        testCaseService.deleteTestCaseStep(testCaseId);
        return ResponseEntity.ok().build();
    }
}
