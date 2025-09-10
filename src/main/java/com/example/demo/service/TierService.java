package com.example.demo.service;

import com.example.demo.Repository.TierRepository;
import com.example.demo.dto.TierDTO;
import com.example.demo.entity.Tier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TierService {

    private final TierRepository tierRepository;

    public TierDTO createTier(TierDTO dto) {
        Tier tier = Tier.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        Tier savedTier = tierRepository.save(tier);
        return convertToDTO(savedTier);
    }

    @Transactional(readOnly = true)
    public List<TierDTO> getAllTiers() {
        return tierRepository.findAllByOrderByTierIdAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TierDTO getTier(Long id) {
        Tier tier = tierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tier not found with id: " + id));
        return convertToDTO(tier);
    }

    public TierDTO updateTier(Long id, TierDTO dto) {
        Tier tier = tierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tier not found with id: " + id));

        tier.setName(dto.getName());
        tier.setDescription(dto.getDescription());

        Tier updatedTier = tierRepository.save(tier);
        return convertToDTO(updatedTier);
    }

    public void deleteTier(Long id) {
        Tier tier = tierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tier not found with id: " + id));
        tierRepository.delete(tier);
    }

    private TierDTO convertToDTO(Tier tier) {
        return TierDTO.builder()
                .tierId(tier.getTierId())
                .name(tier.getName())
                .description(tier.getDescription())
                .build();
    }
}
