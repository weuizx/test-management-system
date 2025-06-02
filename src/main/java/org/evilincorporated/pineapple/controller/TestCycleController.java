package org.evilincorporated.pineapple.controller;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.TestCycleDto;
import org.evilincorporated.pineapple.service.TestCycleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test-cycles")
public class TestCycleController {

    private final TestCycleService testCycleService;

    @PostMapping
    public ResponseEntity<TestCycleDto> createTestCycle(@RequestBody TestCycleDto testCycleDto) {
        return ResponseEntity.ok(testCycleService.createTestCycle(testCycleDto));
    }

    @GetMapping
    public ResponseEntity<List<TestCycleDto>> getAllOfProject(@RequestParam Long projectId) {
        return ResponseEntity.ok(testCycleService.getAllOfProject(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCycleDto> getTestCycle(@PathVariable Long id) {
        return ResponseEntity.ok(testCycleService.getTestCycle(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestCycleDto> updateTestCycle(@PathVariable Long id, @RequestBody TestCycleDto testCycleDto) {
        testCycleDto.setId(id);
        return ResponseEntity.ok(testCycleService.updateTestCycle(testCycleDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCycle(@PathVariable Long id) {
        testCycleService.deleteTestCycle(id);
        return ResponseEntity.ok().build();
    }
}
