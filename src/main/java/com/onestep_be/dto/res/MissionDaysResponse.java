package com.onestep_be.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 미션 완료 일수 응답 DTO
 */
@Schema(description = "미션 완료 일수")
public record MissionDaysResponse(
    
    @Schema(description = "사용자 이름", example = "홍길동")
    String userName,
    
    @Schema(description = "누적 미션 완료 일수", example = "15")
    Integer missionCompletionDays
    
) {
}
