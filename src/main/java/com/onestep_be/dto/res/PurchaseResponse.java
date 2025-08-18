package com.onestep_be.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 구매 응답 DTO
 */
@Schema(description = "상품 구매 응답")
public record PurchaseResponse(
    
    @Schema(description = "구매 성공 여부", example = "true")
    boolean success,
    
    @Schema(description = "바코드 이미지 URL")
    String barcodeImageUrl,
    
    @Schema(description = "구매 후 남은 코인", example = "200")
    Integer remainingCoins
    
) {
}
