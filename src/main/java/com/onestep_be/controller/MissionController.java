package com.onestep_be.controller;

import com.onestep_be.dto.res.MissionResponse;
import com.onestep_be.service.MissionService;
import com.onestep_be.service.MissionCompletionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Mission", description = "미션 관련 API")
@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;
    private final MissionCompletionService missionCompletionService;

    @Operation(summary = "사용자 미션 목록 조회")
    @GetMapping
    public ResponseEntity<List<MissionResponse.Mission>> getUserMissions(
            @RequestHeader("Apple-Token") String appleToken) {
        
        List<MissionResponse.Mission> missions = missionService.getUserMissions(appleToken);
        return ResponseEntity.ok(missions);
    }
    
    @Operation(summary = "미션 완료 인증")
    @PostMapping(value = "/{missionId}/complete", consumes = "multipart/form-data")
    public ResponseEntity<MissionResponse.Completion> completeMission(
            @PathVariable Long missionId,
            @RequestHeader("Apple-Token") String appleToken,
            @RequestParam("image") MultipartFile image) {
        
        MissionResponse.Completion response = missionService.completeMission(missionId, appleToken, image);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
            summary = "사용자 미션 완료 이미지 목록 조회",
            description = "애플 토큰으로 사용자를 찾아 해당 사용자의 미션 완료 이미지들을 최신순으로 반환합니다."
    )
    @GetMapping("/completion-images")
    public ResponseEntity<MissionResponse.CompletionImageList> getMissionCompletionImages(
            @RequestHeader("Apple-Token") String appleToken) {
        
        MissionResponse.CompletionImageList response = missionCompletionService.getUserMissionCompletionImages(appleToken);
        
        return ResponseEntity.ok(response);
    }
}
