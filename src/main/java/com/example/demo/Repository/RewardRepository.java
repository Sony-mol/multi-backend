package com.example.demo.Repository;

import com.example.demo.entity.Reward;
import com.example.demo.entity.enums.RewardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    
    List<Reward> findByIsActiveTrue();
    
    List<Reward> findByType(RewardType type);
    
    List<Reward> findByTypeAndIsActiveTrue(RewardType type);
}
