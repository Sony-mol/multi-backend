package com.example.demo.service;

import com.example.demo.Repository.UserRewardRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Repository.RewardRepository;
import com.example.demo.dto.UserRewardDTO;
import com.example.demo.entity.UserReward;
import com.example.demo.entity.Reward;
import com.example.demo.users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRewardService {

    private final UserRewardRepository userRewardRepository;
    private final UserRepository userRepository;
    private final RewardRepository rewardRepository;

    public UserRewardDTO claimReward(Long userId, Long rewardId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new RuntimeException("Reward not found with id: " + rewardId));

        if (!reward.getIsActive()) {
            throw new RuntimeException("Reward is not active");
        }

        // Check if user already has this reward
        List<UserReward> existingRewards = userRewardRepository.findByUserAndIsClaimed(user, true);
        boolean alreadyClaimed = existingRewards.stream()
                .anyMatch(ur -> ur.getReward().getId().equals(rewardId));

        if (alreadyClaimed) {
            throw new RuntimeException("User has already claimed this reward");
        }

        UserReward userReward = UserReward.builder()
                .user(user)
                .reward(reward)
                .isClaimed(true)
                .claimedAt(new Timestamp(System.currentTimeMillis()))
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        UserReward savedUserReward = userRewardRepository.save(userReward);
        return convertToDTO(savedUserReward);
    }

    @Transactional(readOnly = true)
    public List<UserRewardDTO> getUserRewards(Long userId) {
        return userRewardRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserRewardDTO> getUserClaimedRewards(Long userId) {
        return userRewardRepository.findByUserIdAndIsClaimed(userId, true)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserRewardDTO> getUserUnclaimedRewards(Long userId) {
        return userRewardRepository.findByUserIdAndIsClaimed(userId, false)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserRewardDTO convertToDTO(UserReward userReward) {
        return UserRewardDTO.builder()
                .id(userReward.getId())
                .userId(userReward.getUser().getId())
                .userName(userReward.getUser().getName())
                .rewardId(userReward.getReward().getId())
                .rewardName(userReward.getReward().getName())
                .isClaimed(userReward.getIsClaimed())
                .claimedAt(userReward.getClaimedAt())
                .createdAt(userReward.getCreatedAt())
                .build();
    }
}
