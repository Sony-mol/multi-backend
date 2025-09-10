package com.example.demo.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TierDTO {
    
    private Long tierId;
    private String name;
    private String description;
    private List<com.example.demo.dto.LevelDTO> levels;
}
