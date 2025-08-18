package com.onestep_be.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 쿠폰 응답 DTO
 */
@Schema(description = "쿠폰 정보")
public record CouponResponse(
    
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
    
) {
}
