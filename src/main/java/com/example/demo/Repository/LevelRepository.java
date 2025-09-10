package com.example.demo.Repository;

import com.example.demo.entity.Level;
import com.example.demo.entity.Tier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
    
    List<Level> findByTier(Tier tier);
    
    @Query("SELECT l FROM Level l WHERE l.tier.tierId = :tierId")
    List<Level> findByTierId(Long tierId);
    
    Optional<Level> findByTierAndLevelNumber(Tier tier, Integer levelNumber);
    
    List<Level> findAllByOrderByLevelNumberAsc();
}
