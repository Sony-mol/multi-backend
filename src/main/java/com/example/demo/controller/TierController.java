package com.example.demo.controller;

import com.example.demo.dto.TierDTO;
import com.example.demo.service.TierService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tiers")
@RequiredArgsConstructor
public class TierController {

    private final TierService tierService;

    @PostMapping
    @Operation(summary = "Create Tier")
    public ResponseEntity<TierDTO> createTier(@RequestBody TierDTO dto) {
        return ResponseEntity.ok(tierService.createTier(dto));
    }

    @GetMapping
    @Operation(summary = "Get All Tiers")
    public ResponseEntity<List<TierDTO>> getAllTiers() {
        return ResponseEntity.ok(tierService.getAllTiers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Tier by ID")
    public ResponseEntity<TierDTO> getTier(@PathVariable Long id) {
        return ResponseEntity.ok(tierService.getTier(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Tier")
    public ResponseEntity<TierDTO> updateTier(@PathVariable Long id, @RequestBody TierDTO dto) {
        return ResponseEntity.ok(tierService.updateTier(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Tier")
    public ResponseEntity<Void> deleteTier(@PathVariable Long id) {
        tierService.deleteTier(id);
        return ResponseEntity.noContent().build();
    }
}
