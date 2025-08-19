package com.onestep_be.repository;

import com.onestep_be.entity.MissionCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MissionCompletionRepository extends JpaRepository<MissionCompletion, Long> {
    
    /**
     * 특정 사용자의 미션 완료 기록 조회
     */
    List<MissionCompletion> findByUserId(Long userId);
    
    /**
     * 특정 사용자의 미션 완료 기록을 완료 시간 내림차순으로 조회 (최신순)
     */
    List<MissionCompletion> findByUserIdOrderByCompletedAtDesc(Long userId);
    
    /**
     * 특정 사용자의 특정 미션 완료 기록 조회
     */
    Optional<MissionCompletion> findByUserIdAndMissionId(Long userId, Long missionId);
    
    /**
     * 특정 사용자가 특정 시간 범위에 미션을 완료했는지 확인
     */
    boolean existsByUserIdAndCompletedAtBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime);
}
