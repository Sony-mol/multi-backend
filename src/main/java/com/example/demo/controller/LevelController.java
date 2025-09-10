package com.example.demo.controller;

import com.example.demo.dto.LevelDTO;
import com.example.demo.service.LevelService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/levels")
@RequiredArgsConstructor
public class LevelController {

    private final LevelService levelService;

    @PostMapping
    @Operation(summary = "Create Level")
    public ResponseEntity<LevelDTO> createLevel(@RequestBody LevelDTO dto) {
        return ResponseEntity.ok(levelService.createLevel(dto));
    }

    @GetMapping
    @Operation(summary = "Get All Levels")
    public ResponseEntity<List<LevelDTO>> getAllLevels() {
        return ResponseEntity.ok(levelService.getAllLevels());
    }

    @GetMapping("/tier/{tierId}")
    @Operation(summary = "Get Levels by Tier ID")
    public ResponseEntity<List<LevelDTO>> getLevelsByTier(@PathVariable Long tierId) {
        return ResponseEntity.ok(levelService.getLevelsByTier(tierId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Level by ID")
    public ResponseEntity<LevelDTO> getLevel(@PathVariable Long id) {
        return ResponseEntity.ok(levelService.getLevel(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Level")
    public ResponseEntity<LevelDTO> updateLevel(@PathVariable Long id, @RequestBody LevelDTO dto) {
        return ResponseEntity.ok(levelService.updateLevel(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Level")
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        levelService.deleteLevel(id);
        return ResponseEntity.noContent().build();
    }
}
