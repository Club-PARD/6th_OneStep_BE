package com.onestep_be.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 카테고리 응답 DTO
 */
@Schema(description = "상품 카테고리 응답")
public record CategoryResponse(
    
    @Schema(description = "카테고리 ID", example = "1")
    Long id,
    
    @Schema(description = "카테고리 이름", example = "카페")
    String categoryName
    
) {
}
