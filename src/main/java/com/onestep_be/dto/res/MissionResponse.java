package com.onestep_be.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 미션 조회 응답 DTO
 */
@Schema(description = "미션 조회 응답")
public record MissionResponse(
    
    @Schema(description = "미션 ID", example = "1")
    Long missionId,
    
    @Schema(description = "미션 제목", example = "공원에서 예쁜 꽃 사진 찍기")
    String title,
    
    @Schema(description = "보상 코인", example = "500")
    Integer rewardCoin,
    
    @Schema(description = "인증 완료 여부", example = "true")
    Boolean isCompleted
    
) {
    
    public static MissionResponse of(Long missionId, String title, Integer rewardCoin, Boolean isCompleted) {
        return new MissionResponse(missionId, title, rewardCoin, isCompleted);
    }
}
