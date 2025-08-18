package com.onestep_be.repository;

import com.onestep_be.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    
    /**
     * 사용자별 구매 내역 조회
     */
    List<Purchase> findByUserId(Long userId);
}
