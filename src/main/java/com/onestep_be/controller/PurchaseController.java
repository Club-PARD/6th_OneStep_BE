package com.onestep_be.controller;

import com.onestep_be.dto.res.CouponResponse;
import com.onestep_be.dto.res.ProductResponse;
import com.onestep_be.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Purchase", description = "구매 관련 API")
@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Operation(summary = "상품 구매")
    @PostMapping("/products/{productId}")
    public ResponseEntity<ProductResponse.Purchase> purchaseProduct(
            @PathVariable Long productId,
            @RequestHeader("Apple-Token") String appleToken) {
        
        ProductResponse.Purchase response = purchaseService.purchaseProduct(productId, appleToken);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 쿠폰 목록 조회")
    @GetMapping("/coupons")
    public ResponseEntity<CouponResponse.CouponList> getUserCoupons(
            @RequestHeader("Apple-Token") String appleToken) {
        
        CouponResponse.CouponList response = purchaseService.getUserCoupons(appleToken);
        return ResponseEntity.ok(response);
    }
}
