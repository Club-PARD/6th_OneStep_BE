package com.onestep_be.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * 상품 엔티티
 * 상점의 상품 정보를 관리합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "상품 정보")
public class Product extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @Schema(description = "상품 카테고리")
    private ProductCategory category;
    
    @Column(nullable = false)
    @Schema(description = "상품 이름", example = "스타벅스 아메리카노")
    private String productName;
    
    @Column(nullable = false)
    @Schema(description = "브랜드 이름", example = "스타벅스")
    private String brandName;
    
    @Column(nullable = false, length = 1000)
    @Schema(description = "상품 이미지 URL")
    private String productImageUrl;
    
    @Column(nullable = false)
    @Schema(description = "코인 가격", example = "300")
    private Integer coinPrice;
    
    @Column(nullable = false)
    @Schema(description = "구매 횟수", example = "0")
    private Integer purchaseCount = 0;
    
    /**
     * 구매 횟수 증가
     */
    public void increasePurchaseCount() {
        this.purchaseCount++;
    }

}
