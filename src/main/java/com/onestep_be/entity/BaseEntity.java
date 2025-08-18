package com.onestep_be.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;

/**
 * 기본 엔티티 클래스
 * 모든 엔티티의 공통 ID 필드를 관리합니다.
 */
@MappedSuperclass
@Getter
public abstract class BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "엔티티 ID")
    private Long id;
}
