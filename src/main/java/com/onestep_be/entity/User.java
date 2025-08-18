package com.onestep_be.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 엔티티
 * 애플 토큰으로 사용자를 구분하고 코인 보유량과 미션 연속 일수를 관리합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "사용자 정보")
public class User extends BaseEntity {

    @Column(nullable = false)
    @Schema(description = "이름", example = "사용자1")
    private String name;
    
    @Column(unique = true, nullable = false)
    @Schema(description = "애플 토큰")
    private String appleToken;
    
    @Column(nullable = false)
    @Schema(description = "보유 코인", example = "1000")
    private Integer haveCoin = 0;
    
    @Column(nullable = false)
    @Schema(description = "미션 완료 연속 일수", example = "7")
    private Integer missionStreakDays = 0;
    
    /**
     * 코인 추가
     * 
     * @param amount 추가할 코인 양
     */
    public void addCoin(Integer amount) {
        if (amount > 0) {
            this.haveCoin += amount;
        }
    }
    
    /**
     * 코인 차감
     * 
     * @param amount 차감할 코인 양
     * @throws IllegalArgumentException 보유 코인이 부족한 경우
     */
    public void subtractCoin(Integer amount) {
        if (amount > this.haveCoin) {
            throw new IllegalArgumentException("보유 코인이 부족합니다.");
        }
        this.haveCoin -= amount;
    }
    
    /**
     * 미션 연속 일수 업데이트
     * 
     * @param days 새로운 연속 일수
     */
    public void updateMissionStreakDays(Integer days) {
        this.missionStreakDays = days;
    }
    
}
