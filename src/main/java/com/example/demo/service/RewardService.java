package com.example.demo.service;

import com.example.demo.Repository.RewardRepository;
import com.example.demo.dto.RewardDTO;
import com.example.demo.entity.Reward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RewardService {

    private final RewardRepository rewardRepository;

    public RewardDTO createReward(RewardDTO dto) {
        Reward reward = Reward.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .type(dto.getType())
                .amount(dto.getAmount())
                .points(dto.getPoints())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();

        Reward savedReward = rewardRepository.save(reward);
        return convertToDTO(savedReward);
    }

    @Transactional(readOnly = true)
    public List<RewardDTO> getAllActiveRewards() {
        return rewardRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RewardDTO> getAllRewards() {
        return rewardRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RewardDTO getRewardById(Long id) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found with id: " + id));
        return convertToDTO(reward);
    }

    public RewardDTO updateReward(Long id, RewardDTO dto) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found with id: " + id));

        reward.setName(dto.getName());
        reward.setDescription(dto.getDescription());
        reward.setType(dto.getType());
        reward.setAmount(dto.getAmount());
        reward.setPoints(dto.getPoints());
        reward.setIsActive(dto.getIsActive());

        Reward updatedReward = rewardRepository.save(reward);
        return convertToDTO(updatedReward);
    }

    public void deleteReward(Long id) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found with id: " + id));
        rewardRepository.delete(reward);
    }

    private RewardDTO convertToDTO(Reward reward) {
        return RewardDTO.builder()
                .id(reward.getId())
                .name(reward.getName())
                .description(reward.getDescription())
                .type(reward.getType())
                .amount(reward.getAmount())
                .points(reward.getPoints())
                .isActive(reward.getIsActive())
                .build();
    }
}
