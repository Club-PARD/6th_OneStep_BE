package com.onestep_be.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 쿠폰 목록 응답 DTO
 */
@Schema(description = "쿠폰 저장함 응답")
public record CouponListResponse(
    
    @Schema(description = "쿠폰 목록")
    List<CouponResponse> coupons
    
) {
}
