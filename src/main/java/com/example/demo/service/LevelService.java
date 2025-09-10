package com.example.demo.service;

import com.example.demo.Repository.LevelRepository;
import com.example.demo.Repository.TierRepository;
import com.example.demo.dto.LevelDTO;
import com.example.demo.entity.Level;
import com.example.demo.entity.Tier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LevelService {

    private final LevelRepository levelRepository;
    private final TierRepository tierRepository;

    public LevelDTO createLevel(LevelDTO dto) {
        Tier tier = tierRepository.findById(dto.getTierId())
                .orElseThrow(() -> new RuntimeException("Tier not found with id: " + dto.getTierId()));

        Level level = Level.builder()
                .levelNumber(dto.getLevelNumber())
                .requiredReferrals(dto.getRequiredReferrals())
                .tier(tier)
                .build();

        Level savedLevel = levelRepository.save(level);
        return convertToDTO(savedLevel);
    }

    @Transactional(readOnly = true)
    public List<LevelDTO> getAllLevels() {
        return levelRepository.findAllByOrderByLevelNumberAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LevelDTO> getLevelsByTier(Long tierId) {
        return levelRepository.findByTierId(tierId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LevelDTO getLevel(Long id) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Level not found with id: " + id));
        return convertToDTO(level);
    }

    public LevelDTO updateLevel(Long id, LevelDTO dto) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Level not found with id: " + id));

        level.setLevelNumber(dto.getLevelNumber());
        level.setRequiredReferrals(dto.getRequiredReferrals());

        if (dto.getTierId() != null && !dto.getTierId().equals(level.getTier().getTierId())) {
            Tier tier = tierRepository.findById(dto.getTierId())
                    .orElseThrow(() -> new RuntimeException("Tier not found with id: " + dto.getTierId()));
            level.setTier(tier);
        }

        Level updatedLevel = levelRepository.save(level);
        return convertToDTO(updatedLevel);
    }

    public void deleteLevel(Long id) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Level not found with id: " + id));
        levelRepository.delete(level);
    }

    private LevelDTO convertToDTO(Level level) {
        return LevelDTO.builder()
                .levelId(level.getLevelId())
                .levelNumber(level.getLevelNumber())
                .requiredReferrals(level.getRequiredReferrals())
                .tierId(level.getTier().getTierId())
                .tierName(level.getTier().getName())
                .rewardId(level.getReward() != null ? level.getReward().getId() : null)
                .build();
    }
}
