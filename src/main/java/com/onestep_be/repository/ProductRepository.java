package com.onestep_be.repository;

import com.onestep_be.entity.Product;
import com.onestep_be.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * 카테고리별 상품 조회
     */
    List<Product> findByCategory(ProductCategory category);
    
    /**
     * 카테고리 ID로 상품 조회
     */
    List<Product> findByCategoryId(Long categoryId);
    
    /**
     * 코인 가격 이하 상품 조회
     */
    List<Product> findByCoinPriceLessThanEqual(Integer coinPrice);
    
    /**
     * 카테고리별 인기 상품 조회 (구매횟수 내림차순)
     */
    List<Product> findByCategoryIdOrderByPurchaseCountDesc(Long categoryId);
    
    /**
     * 카테고리별 상품 조회 (가격 낮은순)
     */
    List<Product> findByCategoryIdOrderByCoinPriceAsc(Long categoryId);
    
    /**
     * 카테고리별 상품 조회 (가격 높은순)
     */
    List<Product> findByCategoryIdOrderByCoinPriceDesc(Long categoryId);
    
    /**
     * 현재 코인보다 비싼 상품 중 가장 저렴한 상품 조회 (다음 구매 가능 상품)
     */
    Product findFirstByCoinPriceGreaterThanOrderByCoinPriceAsc(Integer currentCoin);
}
