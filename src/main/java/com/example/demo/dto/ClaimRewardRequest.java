package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimRewardRequest {
    
    private Long userId;
    private Long rewardId;
}
