package com.onestep_be.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "미션 완료 이미지 응답")
public record MissionCompletionImageResponse(
        @Schema(description = "미션 완료 ID", example = "1")
        Long completionId,
        
        @Schema(description = "미션 제목", example = "물 마시기")
        String missionTitle,
        
        @Schema(description = "제출한 이미지 URL", example = "https://s3.amazonaws.com/bucket/image.jpg")
        String imageUrl,
        
        @Schema(description = "완료 시간", example = "2024-01-15T10:30:00")
        LocalDateTime completedAt
) {
}
