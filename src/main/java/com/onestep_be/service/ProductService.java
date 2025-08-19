package com.onestep_be.service;

import com.onestep_be.dto.res.CategoryResponse;
import com.onestep_be.dto.res.ProductResponse;
import com.onestep_be.entity.Product;
import com.onestep_be.entity.ProductCategory;
import com.onestep_be.entity.User;
import com.onestep_be.exception.GlobalException;
import com.onestep_be.repository.ProductCategoryRepository;
import com.onestep_be.repository.ProductRepository;
import com.onestep_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final UserRepository userRepository;


    /**
     * 카테고리별 상품 조회
     */
    public List<ProductResponse.Product> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        
        return products.stream()
                .map(product -> new ProductResponse.Product(
                        product.getId(),
                        product.getProductName(),
                        product.getBrandName(),
                        product.getProductImageUrl(),
                        product.getCoinPrice(),
                        product.getCategory().getCategoryName()
                ))
                .toList();
    }

    /**
     * 사용자 코인으로 구매 가능한 상품 조회 (가나다순 정렬)
     */
    public List<ProductResponse.Product> getAffordableProducts(String appleToken) {
        // 사용자 조회
        User user = userRepository.findByAppleToken(appleToken)
                .orElseThrow(() -> GlobalException.notFound("사용자"));
        
        // 사용자 코인으로 구매 가능한 상품 조회
        List<Product> products = productRepository.findByCoinPriceLessThanEqual(user.getHaveCoin());
        
        return products.stream()
                .map(product -> new ProductResponse.Product(
                        product.getId(),
                        product.getProductName(),
                        product.getBrandName(),
                        product.getProductImageUrl(),
                        product.getCoinPrice(),
                        product.getCategory().getCategoryName()
                ))
                .sorted((p1, p2) -> p1.productName().compareTo(p2.productName())) // 가나다순 정렬
                .toList();
    }

    /**
     * 카테고리별 인기 상품 조회 (구매횟수 내림차순)
     */
    public List<ProductResponse.Product> getPopularProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryIdOrderByPurchaseCountDesc(categoryId);
        
        return products.stream()
                .map(product -> new ProductResponse.Product(
                        product.getId(),
                        product.getProductName(),
                        product.getBrandName(),
                        product.getProductImageUrl(),
                        product.getCoinPrice(),
                        product.getCategory().getCategoryName()
                ))
                .toList();
    }

    /**
     * 카테고리별 상품 조회 (가격 낮은순)
     */
    public List<ProductResponse.Product> getProductsByCategoryOrderByPriceLow(Long categoryId) {
        List<Product> products = productRepository.findByCategoryIdOrderByCoinPriceAsc(categoryId);
        
        return products.stream()
                .map(product -> new ProductResponse.Product(
                        product.getId(),
                        product.getProductName(),
                        product.getBrandName(),
                        product.getProductImageUrl(),
                        product.getCoinPrice(),
                        product.getCategory().getCategoryName()
                ))
                .toList();
    }

    /**
     * 카테고리별 상품 조회 (가격 높은순)
     */
    public List<ProductResponse.Product> getProductsByCategoryOrderByPriceHigh(Long categoryId) {
        List<Product> products = productRepository.findByCategoryIdOrderByCoinPriceDesc(categoryId);
        
        return products.stream()
                .map(product -> new ProductResponse.Product(
                        product.getId(),
                        product.getProductName(),
                        product.getBrandName(),
                        product.getProductImageUrl(),
                        product.getCoinPrice(),
                        product.getCategory().getCategoryName()
                ))
                .toList();
    }

}
