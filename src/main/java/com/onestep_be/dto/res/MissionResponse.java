package com.onestep_be.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 미션 관련 응답 DTO 클래스
 */
public class MissionResponse {

    /**
     * 미션 조회 응답 DTO
     */
    @Schema(description = "미션 조회 응답")
    public record Mission(
        @Schema(description = "미션 ID", example = "1")
        Long missionId,
        
        @Schema(description = "미션 제목", example = "공원에서 예쁜 꽃 사진 찍기")
        String title,
        
        @Schema(description = "보상 코인", example = "500")
        Integer rewardCoin,
        
        @Schema(description = "인증 완료 여부", example = "true")
        Boolean isCompleted
    ) {
        public static Mission of(Long missionId, String title, Integer rewardCoin, Boolean isCompleted) {
            return new Mission(missionId, title, rewardCoin, isCompleted);
        }
    }

    /**
     * 미션 완료 제출 응답 DTO
     */
    @Getter
    @Builder
    @Schema(description = "미션 완료 응답")
    public static class Completion {
        private final Integer rewardCoin;
        private final Boolean isCompleted;
        private final String submittedImageUrl;
        
        public static Completion success(Integer rewardCoin, String imageUrl) {
            return Completion.builder()
                    .rewardCoin(rewardCoin)
                    .isCompleted(true)
                    .submittedImageUrl(imageUrl)
                    .build();
        }
    }

    /**
     * 개별 미션 완료 이미지 응답 DTO
     */
    @Builder
    @Schema(description = "미션 완료 이미지 응답")
    public record CompletionImage(
        @Schema(description = "미션 완료 ID", example = "1")
        Long completionId,
        
        @Schema(description = "미션 제목", example = "물 마시기")
        String missionTitle,
        
        @Schema(description = "제출한 이미지 URL", example = "https://s3.amazonaws.com/bucket/image.jpg")
        String imageUrl
    ) {}

    /**
     * 미션 완료 이미지 목록 응답 DTO
     */
    @Builder
    @Schema(description = "미션 완료 이미지 목록 응답")
    public record CompletionImageList(
        @Schema(description = "미션 완료 이미지 목록")
        List<CompletionImage> images
    ) {}

    /**
     * 미션 완료 일수 응답 DTO
     */
    @Schema(description = "미션 완료 일수")
    public record Days(
        @Schema(description = "사용자 이름", example = "홍길동")
        String userName,
        
        @Schema(description = "누적 미션 완료 일수", example = "15")
        Integer missionCompletionDays
    ) {}
}
