package com.onestep_be.controller;

import com.onestep_be.dto.res.MissionResponse;
import com.onestep_be.dto.res.MissionCompletionResponse;
import com.onestep_be.service.MissionService;
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

    @Operation(summary = "사용자 미션 목록 조회")
    @GetMapping
    public ResponseEntity<List<MissionResponse>> getUserMissions(
            @RequestHeader("Apple-Token") String appleToken) {
        
        List<MissionResponse> missions = missionService.getUserMissions(appleToken);
        return ResponseEntity.ok(missions);
    }
    
    @Operation(summary = "미션 완료 인증")
    @PostMapping(value = "/{missionId}/complete", consumes = "multipart/form-data")
    public ResponseEntity<MissionCompletionResponse> completeMission(
            @PathVariable Long missionId,
            @RequestHeader("Apple-Token") String appleToken,
            @RequestParam("image") MultipartFile image) {
        
        MissionCompletionResponse response = missionService.completeMission(missionId, appleToken, image);
        return ResponseEntity.ok(response);
    }
}
