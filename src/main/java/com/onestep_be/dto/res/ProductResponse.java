package com.onestep_be.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * 상품 및 구매 관련 응답 DTO 클래스
 */
public class ProductResponse {

    /**
     * 개별 상품 정보 응답 DTO
     */
    @Builder
    @Schema(description = "상품 정보 응답")
    public record Product(
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
    ) {}

    /**
     * 상품 목록 응답 DTO
     */
    @Builder
    @Schema(description = "상품 목록 응답")
    public record ProductList(
        @Schema(description = "상품 목록")
        List<Product> products
    ) {}

    /**
     * 구매 응답 DTO
     */
    @Builder
    @Schema(description = "상품 구매 응답")
    public record Purchase(
        @Schema(description = "구매 성공 여부", example = "true")
        boolean success,
        
        @Schema(description = "바코드 이미지 URL")
        String barcodeImageUrl,
        
        @Schema(description = "구매 후 남은 코인", example = "200")
        Integer remainingCoins
    ) {}
}
