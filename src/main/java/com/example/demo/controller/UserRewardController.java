package com.example.demo.controller;

import com.example.demo.dto.ClaimRewardRequest;
import com.example.demo.dto.UserRewardDTO;
import com.example.demo.service.UserRewardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-rewards")
@RequiredArgsConstructor
public class UserRewardController {

    private final UserRewardService userRewardService;

    @PostMapping("/claim")
    @Operation(summary = "Claim a reward")
    public ResponseEntity<UserRewardDTO> claimReward(@RequestBody ClaimRewardRequest request) {
        return ResponseEntity.ok(userRewardService.claimReward(request.getUserId(), request.getRewardId()));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all rewards for a user")
    public ResponseEntity<List<UserRewardDTO>> getUserRewards(@PathVariable Long userId) {
        return ResponseEntity.ok(userRewardService.getUserRewards(userId));
    }

    @GetMapping("/user/{userId}/claimed")
    @Operation(summary = "Get claimed rewards for a user")
    public ResponseEntity<List<UserRewardDTO>> getUserClaimedRewards(@PathVariable Long userId) {
        return ResponseEntity.ok(userRewardService.getUserClaimedRewards(userId));
    }

    @GetMapping("/user/{userId}/unclaimed")
    @Operation(summary = "Get unclaimed rewards for a user")
    public ResponseEntity<List<UserRewardDTO>> getUserUnclaimedRewards(@PathVariable Long userId) {
        return ResponseEntity.ok(userRewardService.getUserUnclaimedRewards(userId));
    }
}
