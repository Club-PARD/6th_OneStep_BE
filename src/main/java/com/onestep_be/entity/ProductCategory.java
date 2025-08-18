package com.onestep_be.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * 상품 카테고리 엔티티
 * 상점의 상품 카테고리를 관리합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "상품 카테고리 정보")
public class ProductCategory extends BaseEntity {
    
    @Column(nullable = false, unique = true)
    @Schema(description = "카테고리 이름", example = "식사")
    private String categoryName;
    
}
