package com.onestep_be.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 사용자 로그인 요청 DTO
 */
@Schema(description = "사용자 로그인 요청")
public record UserLoginRequest(
    
    @NotBlank(message = "사용자 이름은 필수입니다")
    @Schema(description = "사용자 이름", example = "사용자1")
    String name
    
) {}
