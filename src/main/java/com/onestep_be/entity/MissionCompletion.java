package com.onestep_be.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * 미션 완료 엔티티
 * 사용자의 미션 완료 기록을 관리합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "미션 완료 정보")
public class MissionCompletion extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "사용자")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    @Schema(description = "미션")
    private Mission mission;
    
    @Column(nullable = false, length = 1000)
    @Schema(description = "제출한 이미지 URL")
    @Setter
    private String submittedImageUrl;
    
    @Column(nullable = false)
    @Schema(description = "인증 완료 여부", example = "false")
    private Boolean isCompleted = false;
    
    @Column(nullable = false, updatable = false)
    @Schema(description = "완료 시간")
    private java.time.LocalDateTime completedAt;
    
    /**
     * 미션 완료 처리
     */
    public void complete() {
        this.isCompleted = true;
    }
    
    /**
     * 미션 미완료 처리
     */
    public void incomplete() {
        this.isCompleted = false;
    }
    
    @PrePersist //DB 저장 직전 시간 기록 
    protected void onCreate() {
        this.completedAt = java.time.LocalDateTime.now();
        if (this.isCompleted == null) {
            this.isCompleted = false;
        }
    }
}
