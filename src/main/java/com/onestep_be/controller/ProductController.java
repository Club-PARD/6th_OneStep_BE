package com.onestep_be.controller;

import com.onestep_be.dto.res.CategoryResponse;
import com.onestep_be.dto.res.ProductResponse;
import com.onestep_be.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @Operation(summary = "서버 테스트용")
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @PathVariable Long categoryId) {
        
        List<ProductResponse> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "구매 가능한 상품 조회 (가나다순)")
    @GetMapping("/affordable")
    public ResponseEntity<List<ProductResponse>> getAffordableProducts(
            @RequestHeader("Apple-Token") String appleToken) {
        
        List<ProductResponse> products = productService.getAffordableProducts(appleToken);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "카테고리별 인기 상품 조회 (기본조회)")
    @GetMapping("/categories/{categoryId}/popular")
    public ResponseEntity<List<ProductResponse>> getPopularProductsByCategory(
            @PathVariable Long categoryId) {
        
        List<ProductResponse> products = productService.getPopularProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "카테고리별 상품 조회 (가격 낮은순)")
    @GetMapping("/categories/{categoryId}/price-low")
    public ResponseEntity<List<ProductResponse>> getProductsByCategoryOrderByPriceLow(
            @PathVariable Long categoryId) {
        
        List<ProductResponse> products = productService.getProductsByCategoryOrderByPriceLow(categoryId);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "카테고리별 상품 조회 (가격 높은순)")
    @GetMapping("/categories/{categoryId}/price-high")
    public ResponseEntity<List<ProductResponse>> getProductsByCategoryOrderByPriceHigh(
            @PathVariable Long categoryId) {
        
        List<ProductResponse> products = productService.getProductsByCategoryOrderByPriceHigh(categoryId);
        return ResponseEntity.ok(products);
    }
}
