package com.onestep_be.controller;

import com.onestep_be.dto.res.CouponListResponse;
import com.onestep_be.dto.res.PurchaseResponse;
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
    public ResponseEntity<PurchaseResponse> purchaseProduct(
            @PathVariable Long productId,
            @RequestHeader("Apple-Token") String appleToken) {
        
        PurchaseResponse response = purchaseService.purchaseProduct(productId, appleToken);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 쿠폰 목록 조회")
    @GetMapping("/coupons")
    public ResponseEntity<CouponListResponse> getUserCoupons(
            @RequestHeader("Apple-Token") String appleToken) {
        
        CouponListResponse response = purchaseService.getUserCoupons(appleToken);
        return ResponseEntity.ok(response);
    }
}
