package com.onestep_be.service;

import com.onestep_be.dto.res.MissionResponse;
import com.onestep_be.entity.MissionCompletion;
import com.onestep_be.entity.User;
import com.onestep_be.repository.MissionCompletionRepository;
import com.onestep_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionCompletionService {

    private final MissionCompletionRepository missionCompletionRepository;
    private final UserRepository userRepository;

    /**
     * 사용자의 미션 완료 이미지 목록 조회 (최신순)
     */
    public MissionResponse.CompletionImageList getUserMissionCompletionImages(String appleToken) {
        // 사용자 조회
        User user = userRepository.findByAppleToken(appleToken)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        // 미션 완료 기록을 최신순으로 조회
        List<MissionCompletion> completions = missionCompletionRepository
                .findByUserIdOrderByCompletedAtDesc(user.getId());

        // DTO 변환
        List<MissionResponse.CompletionImage> imageResponses = completions.stream()
                .map(this::convertToImageResponse)
                .toList();

        log.info("사용자 미션 완료 이미지 조회: userId={}, 이미지 개수={}", 
                user.getId(), imageResponses.size());

        return MissionResponse.CompletionImageList.builder()
                .images(imageResponses)
                .build();
    }

    /**
     * MissionCompletion을 MissionCompletionImageResponse로 변환
     */
    private MissionResponse.CompletionImage convertToImageResponse(MissionCompletion completion) {
        return MissionResponse.CompletionImage.builder()
                .completionId(completion.getId())
                .missionTitle(completion.getMission().getTitle())
                .imageUrl(completion.getSubmittedImageUrl())
                .build();
    }
}
