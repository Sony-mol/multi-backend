package com.example.demo.dto;

import com.example.demo.entity.enums.RewardType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardDTO {
    
    private Long id;
    private String name;
    private String description;
    private RewardType type;
    private BigDecimal amount;
    private Integer points;
    private Boolean isActive;
}
