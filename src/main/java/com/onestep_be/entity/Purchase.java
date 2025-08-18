package com.onestep_be.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * 구매 엔티티
 * 사용자의 상품 구매 기록을 관리합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "구매 정보")
public class Purchase extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "구매자")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @Schema(description = "구매한 상품")
    private Product product;
    
    @Column(nullable = false)
    @Schema(description = "사용한 코인", example = "300")
    private Integer coinSpent;
    
    @Column(nullable = false, length = 1000)
    @Schema(description = "바코드 쿠폰 이미지 URL")
    private String barcodeImageUrl;
    
    @Column(nullable = false)
    @Schema(description = "쿠폰 사용 여부", example = "false")
    private Boolean isUsed = false;
    
    /**
     * 쿠폰 사용 처리
     */
    public void useCoupon() {
        this.isUsed = true;
    }
    
    /**
     * 쿠폰 사용 취소 (필요시)
     */
    public void cancelCouponUsage() {
        this.isUsed = false;
    }
}
