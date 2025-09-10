package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "levels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Level {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long levelId;

    private Integer levelNumber;
    private Integer requiredReferrals;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_id")
    @JsonIgnoreProperties({"levels"})
    private Tier tier;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reward_id")
    private Reward reward; // optional snapshot reward template
}
