package com.onestep_be.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "미션 완료 이미지 목록 응답")
public record MissionCompletionImageListResponse(
        @Schema(description = "미션 완료 이미지 목록")
        List<MissionCompletionImageResponse> images
) {
}
