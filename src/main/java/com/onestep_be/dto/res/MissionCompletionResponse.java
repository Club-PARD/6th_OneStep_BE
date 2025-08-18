package com.onestep_be.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionCompletionResponse {
    
    private final Integer rewardCoin;
    private final Boolean isCompleted;
    private final String submittedImageUrl;
    
    public static MissionCompletionResponse success(Integer rewardCoin, String imageUrl) {
        return MissionCompletionResponse.builder()
                .rewardCoin(rewardCoin)
                .isCompleted(true)
                .submittedImageUrl(imageUrl)
                .build();
    }
}
