package com.onestep_be.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * 미션 엔티티
 * 주간미션과 일일미션을 관리합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "미션 정보")
public class Mission extends BaseEntity {
    
    @Column(nullable = false)
    @Schema(description = "미션 제목", example = "공원에서 예쁜 꽃 사진 찍기")
    private String title;
    
    @Column(nullable = false)
    @Schema(description = "보상 코인", example = "500")
    private Integer rewardCoin;
    
    
    
}
