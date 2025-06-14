package org.evilincorporated.pineapple.controller;

import lombok.RequiredArgsConstructor;
import org.evilincorporated.pineapple.controller.dto.ReleaseDto;
import org.evilincorporated.pineapple.service.ReleaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/releases")
public class ReleaseController {

    private final ReleaseService releaseService;

    @PostMapping
    public ResponseEntity<ReleaseDto> createRelease(@RequestBody ReleaseDto releaseDto) {
        return ResponseEntity.ok(releaseService.createRelease(releaseDto));
    }

    @GetMapping
    public ResponseEntity<List<ReleaseDto>> getAllOfProject(@RequestParam Long projectId) {
        return ResponseEntity.ok(releaseService.getAllOfProject(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReleaseDto> getRelease(@PathVariable Long id) {
        return ResponseEntity.ok(releaseService.getRelease(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReleaseDto> updateRelease(@PathVariable Long id, @RequestBody ReleaseDto releaseDto) {
        releaseDto.setId(id);
        return ResponseEntity.ok(releaseService.updateRelease(releaseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelease(@PathVariable Long id) {
        releaseService.deleteRelease(id);
        return ResponseEntity.ok().build();
    }
}
