package com.onestep_be.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 상품 응답 DTO
 */
@Schema(description = "상품 정보 응답")
public record ProductResponse(
    
    @Schema(description = "상품 ID", example = "1")
    Long id,
    
    @Schema(description = "상품 이름", example = "스타벅스 아메리카노")
    String productName,
    
    @Schema(description = "브랜드 이름", example = "스타벅스")
    String brandName,
    
    @Schema(description = "상품 이미지 URL")
    String productImageUrl,
    
    @Schema(description = "코인 가격", example = "300")
    Integer coinPrice,
    
    @Schema(description = "카테고리 이름", example = "카페")
    String categoryName
    
) {
}
