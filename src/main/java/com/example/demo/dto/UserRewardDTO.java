package com.example.demo.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRewardDTO {
    
    private Long id;
    private Long userId;
    private String userName;
    private Long rewardId;
    private String rewardName;
    private Boolean isClaimed;
    private Timestamp claimedAt;
    private Timestamp createdAt;
}
