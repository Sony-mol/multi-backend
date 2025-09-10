package com.example.demo.controller;

import com.example.demo.dto.RewardDTO;
import com.example.demo.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @PostMapping
    @Operation(summary = "Create Reward")
    public ResponseEntity<RewardDTO> createReward(@RequestBody RewardDTO dto) {
        return ResponseEntity.ok(rewardService.createReward(dto));
    }

    @GetMapping
    @Operation(summary = "Get All Active Rewards")
    public ResponseEntity<List<RewardDTO>> getAllActiveRewards() {
        return ResponseEntity.ok(rewardService.getAllActiveRewards());
    }

    @GetMapping("/all")
    @Operation(summary = "Get All Rewards")
    public ResponseEntity<List<RewardDTO>> getAllRewards() {
        return ResponseEntity.ok(rewardService.getAllRewards());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Reward by ID")
    public ResponseEntity<RewardDTO> getRewardById(@PathVariable Long id) {
        return ResponseEntity.ok(rewardService.getRewardById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Reward")
    public ResponseEntity<RewardDTO> updateReward(@PathVariable Long id, @RequestBody RewardDTO dto) {
        return ResponseEntity.ok(rewardService.updateReward(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Reward")
    public ResponseEntity<Void> deleteReward(@PathVariable Long id) {
        rewardService.deleteReward(id);
        return ResponseEntity.noContent().build();
    }
}
