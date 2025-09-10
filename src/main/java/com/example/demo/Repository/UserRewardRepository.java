package com.example.demo.Repository;

import com.example.demo.entity.UserReward;
import com.example.demo.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRewardRepository extends JpaRepository<UserReward, Long> {
    
    List<UserReward> findByUser(Users user);
    
    List<UserReward> findByUserAndIsClaimed(Users user, Boolean isClaimed);
    
    List<UserReward> findByUserId(Long userId);
    
    List<UserReward> findByUserIdAndIsClaimed(Long userId, Boolean isClaimed);
}
