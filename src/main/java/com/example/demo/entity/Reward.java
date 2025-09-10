package com.example.demo.entity;

import com.example.demo.entity.enums.RewardType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "rewards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType type;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column
    private Integer points;

    @Column
    @Builder.Default
    private Boolean isActive = true;
}
