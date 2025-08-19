package com.onestep_be.service;

import com.onestep_be.dto.res.MissionResponse;
import java.util.Set;
import java.util.stream.Collectors;
import com.onestep_be.entity.Mission;
import com.onestep_be.entity.MissionCompletion;
import com.onestep_be.entity.User;
import com.onestep_be.exception.GlobalException;
import com.onestep_be.repository.MissionCompletionRepository;
import com.onestep_be.repository.MissionRepository;
import com.onestep_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionService {

    private final MissionRepository missionRepository;
    private final MissionCompletionRepository missionCompletionRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final OpenAIService openAIService;

    /**
     * 사용자별 미션 목록 조회 (홈 화면용) - 모든 미션 표시
     */
    public List<MissionResponse.Mission> getUserMissions(String appleToken) {
        // 사용자 조회
        User user = validateUser(appleToken);
        
        // 모든 미션 조회
        List<Mission> allMissions = missionRepository.findAll();
        
        // 사용자가 완료한 미션 조회
        List<MissionCompletion> missionCompletions = missionCompletionRepository.findByUserId(user.getId());
        
        // 완료된 미션 ID를 Set으로 변환 (빠른 검색을 위해)
        Set<Long> completedMissionIds = missionCompletions.stream()
                .map(completion -> completion.getMission().getId())
                .collect(Collectors.toSet());
        
        // 모든 미션에 대해 완료 여부를 확인하여 응답 생성
        return allMissions.stream()
                .map(mission -> MissionResponse.Mission.of(
                        mission.getId(),
                        mission.getTitle(),
                        mission.getRewardCoin(),
                        completedMissionIds.contains(mission.getId())
                ))
                .toList();
    }
    
    /**
     * 미션 완료 제출
     */
    @Transactional
    public MissionResponse.Completion completeMission(Long missionId, String appleToken, MultipartFile imageFile) {
        User user = validateUser(appleToken);
        Mission mission = validateMission(missionId);
        
        // 이미 완료한 미션인지 확인
        validateMissionNotCompleted(user.getId(), missionId);
        
        String uploadedImageUrl = uploadAndVerifyImage(imageFile, user.getId(), mission.getTitle());
        
        MissionCompletion completion = processCompletionRecord(user, mission, uploadedImageUrl);
        rewardUser(user, mission.getRewardCoin());
        updateDailyMissionCount(user);
        
        saveCompletionData(completion, user);
        
        log.info("미션 완료: 사용자={}, 미션={}, 보상코인={}", 
                user.getId(), mission.getId(), mission.getRewardCoin());
        
        return MissionResponse.Completion.success(
                mission.getRewardCoin(), 
                uploadedImageUrl
        );
    }
    
    /**
     * 사용자 검증
     */
    private User validateUser(String appleToken) {
        return userRepository.findByAppleToken(appleToken)
                .orElseThrow(() -> GlobalException.notFound("사용자"));
    }
    
    /**
     * 미션 검증
     */
    private Mission validateMission(Long missionId) {
        return missionRepository.findById(missionId)
                .orElseThrow(() -> GlobalException.notFound("미션"));
    }
    
    /**
     * 이미지 검증 후 업로드
     */
    private String uploadAndVerifyImage(MultipartFile imageFile, Long userId, String missionTitle) {
        // 1. 먼저 임시 업로드로 GPT 검증
        String tempImageUrl = s3Service.uploadImage(imageFile, userId);
        
        // 2. GPT로 이미지 검증
        boolean isValidImage = verifyImageWithGPT(missionTitle, tempImageUrl);
        if (!isValidImage) {
            // 검증 실패시 S3에서 이미지 삭제
            s3Service.deleteImage(tempImageUrl);
            throw GlobalException.badRequest("제출한 이미지가 미션 요구사항에 맞지 않습니다");
        }
        
        // 3. 검증 성공시 URL 반환
        return tempImageUrl;
    }
    
    /**
     * 미션 완료 기록 처리
     */
    private MissionCompletion processCompletionRecord(User user, Mission mission, String imageUrl) {
        MissionCompletion completion = missionCompletionRepository.findByUserId(user.getId())
                .stream()
                .filter(mc -> mc.getMission().getId().equals(mission.getId()))
                .findFirst()
                .orElse(MissionCompletion.builder()
                        .user(user)
                        .mission(mission)
                        .submittedImageUrl(imageUrl)
                        .isCompleted(false)
                        .build());
        
        completion.setSubmittedImageUrl(imageUrl);
        completion.complete();
        
        return completion;
    }
    
    /**
     * 사용자 코인 지급
     */
    private void rewardUser(User user, Integer rewardCoin) {
        user.addCoin(rewardCoin);
    }
    
    /**
     * 완료 데이터 저장
     */
    /**
     * 이미 완료한 미션인지 검증
     */
    private void validateMissionNotCompleted(Long userId, Long missionId) {
        boolean alreadyCompleted = missionCompletionRepository
                .findByUserIdAndMissionId(userId, missionId)
                .map(MissionCompletion::getIsCompleted)
                .orElse(false);
        
        if (alreadyCompleted) {
            throw GlobalException.badRequest("이미 완료한 미션입니다");
        }
    }

    private void saveCompletionData(MissionCompletion completion, User user) {
        missionCompletionRepository.save(completion);
        userRepository.save(user);
    }
    
    /**
     * 일일 미션 완료 횟수 업데이트 (User 테이블의 누적 일수 증가)
     */
    private void updateDailyMissionCount(User user) {
        LocalDate today = LocalDate.now();
        
        // 오늘 이미 미션을 완료한 적이 있는지 확인
        boolean alreadyCompletedToday = missionCompletionRepository
                .existsByUserIdAndCompletedAtBetween(
                        user.getId(),
                        today.atStartOfDay(),
                        today.atTime(23, 59, 59)
                );
        
        // 오늘 첫 미션 완료라면 누적 일수 증가
        if (!alreadyCompletedToday) {
            user.updateMissionStreakDays(user.getMissionStreakDays() + 1);
            log.info("미션 누적 일수 증가: userId={}, 새로운 누적일수={}", 
                    user.getId(), user.getMissionStreakDays());
        }
    }

    /**
     * GPT로 이미지 검증
     */
    private boolean verifyImageWithGPT(String missionTitle, String imageUrl) {
        return openAIService.verifyImageWithGPT(missionTitle, imageUrl);
    }
}
