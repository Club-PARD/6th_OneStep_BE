package com.onestep_be.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * 쿠폰 관련 응답 DTO 클래스
 */
public class CouponResponse {

    /**
     * 개별 쿠폰 정보 응답 DTO
     */
    @Builder
    @Schema(description = "쿠폰 정보")
    public record Coupon(
        @Schema(description = "구매 ID", example = "1")
        Long id,
        
        @Schema(description = "상품 이름", example = "스타벅스 아메리카노")
        String productName,
        
        @Schema(description = "브랜드 이름", example = "스타벅스")
        String brandName,
        
        @Schema(description = "상품 이미지 URL")
        String productImageUrl,
        
        @Schema(description = "바코드 이미지 URL")
        String barcodeImageUrl,
        
        @Schema(description = "코인 가격", example = "300")
        Integer coinPrice
    ) {}

    /**
     * 쿠폰 목록 응답 DTO
     */
    @Builder
    @Schema(description = "쿠폰 저장함 응답")
    public record CouponList(
        @Schema(description = "쿠폰 목록")
        List<Coupon> coupons
    ) {}
}
