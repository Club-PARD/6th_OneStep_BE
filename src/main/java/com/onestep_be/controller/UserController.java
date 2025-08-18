package com.onestep_be.controller;

import com.onestep_be.dto.req.UserLoginRequest;
import com.onestep_be.dto.res.MissionDaysResponse;
import com.onestep_be.dto.res.UserCoinResponse;
import com.onestep_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 로그인")
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestHeader("Apple-Token") String appleToken,
            @Valid @RequestBody UserLoginRequest request) {
        
        userService.login(appleToken, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "미션 완료 일수 조회 + 이름 조회")
    @GetMapping("/mission-days")
    public ResponseEntity<MissionDaysResponse> getMissionDays(
            @RequestHeader("Apple-Token") String appleToken) {
        
        MissionDaysResponse response = userService.getMissionDays(appleToken);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "사용자 코인 조회 + 다음 상품까지 남은 코인")
    @GetMapping("/coin")
    public ResponseEntity<UserCoinResponse> getUserCoin(
            @RequestHeader("Apple-Token") String appleToken) {
        
        UserCoinResponse response = userService.getUserCoin(appleToken);
        return ResponseEntity.ok(response);
    }
}
