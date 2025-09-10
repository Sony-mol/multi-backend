package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LevelDTO {
    
    private Long levelId;
    private Integer levelNumber;
    private Integer requiredReferrals;
    private Long tierId;
    private String tierName;
    private Long rewardId;
}
