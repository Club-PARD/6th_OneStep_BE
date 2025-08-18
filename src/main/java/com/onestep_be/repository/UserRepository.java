package com.onestep_be.repository;

import com.onestep_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 애플 토큰으로 사용자 조회
     */
    Optional<User> findByAppleToken(String appleToken);
    
    /**
     * 애플 토큰 존재 여부 확인
     */
    boolean existsByAppleToken(String appleToken);
}
