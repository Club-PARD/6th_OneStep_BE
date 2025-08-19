package com.onestep_be.service;

import com.onestep_be.dto.res.CouponListResponse;
import com.onestep_be.dto.res.CouponResponse;
import com.onestep_be.dto.res.PurchaseResponse;
import com.onestep_be.entity.Product;
import com.onestep_be.entity.Purchase;
import com.onestep_be.entity.User;
import com.onestep_be.exception.GlobalException;
import com.onestep_be.repository.ProductRepository;
import com.onestep_be.repository.PurchaseRepository;
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
public class PurchaseService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    
    // 고정 바코드 이미지 URL 
    private static final String BARCODE_IMAGE_URL = "https://onestep-mission-images.s3.ap-northeast-2.amazonaws.com/Frame+2085666593+(1).png";

    /**
     * 상품 구매
     */
    @Transactional
    public PurchaseResponse purchaseProduct(Long productId, String appleToken) {
        // 사용자 조회
        User user = userRepository.findByAppleToken(appleToken)
                .orElseThrow(() -> GlobalException.notFound("사용자"));
        
        // 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> GlobalException.notFound("상품"));
        
        // 코인 부족 확인
        if (user.getHaveCoin() < product.getCoinPrice()) {
            return new PurchaseResponse(false, null, user.getHaveCoin());
        }
        
        // 코인 차감
        user.subtractCoin(product.getCoinPrice());
        
        // 상품 구매 횟수 증가
        product.increasePurchaseCount();
        
        // 구매 기록 저장
        Purchase purchase = Purchase.builder()
                .user(user)
                .product(product)
                .coinSpent(product.getCoinPrice())
                .barcodeImageUrl(BARCODE_IMAGE_URL)
                .isUsed(false)
                .build();
        
        purchaseRepository.save(purchase);
        productRepository.save(product);
        userRepository.save(user);
        
        log.info("상품 구매 완료: 사용자={}, 상품={}, 가격={}, 남은코인={}", 
                user.getId(), product.getId(), product.getCoinPrice(), user.getHaveCoin());
        
        return new PurchaseResponse(true, BARCODE_IMAGE_URL, user.getHaveCoin());
    }

    /**
     * 사용자 쿠폰 목록 조회
     */
    public CouponListResponse getUserCoupons(String appleToken) {
        // 사용자 조회
        User user = userRepository.findByAppleToken(appleToken)
                .orElseThrow(() -> GlobalException.notFound("사용자"));
        
        // 사용자 구매 내역 조회
        List<Purchase> purchases = purchaseRepository.findByUserId(user.getId());
        
        List<CouponResponse> coupons = purchases.stream()
                .map(purchase -> new CouponResponse(
                        purchase.getId(),
                        purchase.getProduct().getProductName(),
                        purchase.getProduct().getBrandName(),
                        purchase.getProduct().getProductImageUrl(),
                        purchase.getBarcodeImageUrl(),
                        purchase.getProduct().getCoinPrice()
                ))
                .toList();
        
        return new CouponListResponse(coupons, coupons.size());
    }
}
