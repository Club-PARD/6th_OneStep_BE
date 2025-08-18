package com.onestep_be.service;

import com.onestep_be.dto.req.UserLoginRequest;
import com.onestep_be.dto.res.MissionDaysResponse;
import com.onestep_be.dto.res.UserCoinResponse;
import com.onestep_be.entity.Product;
import com.onestep_be.entity.User;
import com.onestep_be.repository.ProductRepository;
import com.onestep_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void login(String appleToken, UserLoginRequest request) {
        // 애플 토큰으로 기존 사용자 조회
        User user = userRepository.findByAppleToken(appleToken)
                .orElseGet(() -> createNewUser(appleToken, request));
        
        log.debug("사용자 로그인 완료: userId={}", user.getId());
    }

    private User createNewUser(String appleToken, UserLoginRequest request) {
        User newUser = User.builder()
                .appleToken(appleToken)
                .name(request.name())
                .haveCoin(0)
                .missionStreakDays(0)
                .build();
        
        User savedUser = userRepository.save(newUser);
        log.info("새 사용자 생성: userId={}, name={}", savedUser.getId(), request.name());
        
        return savedUser;
    }

    /**
     * 사용자 미션 완료 일수 조회 (User 테이블에서 직접 조회)
     */
    public MissionDaysResponse getMissionDays(String appleToken) {
        User user = userRepository.findByAppleToken(appleToken)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        return new MissionDaysResponse(user.getName(), user.getMissionStreakDays());
    }
    
    /**
     * 사용자 코인 조회
     */
    public UserCoinResponse getUserCoin(String appleToken) {
        User user = userRepository.findByAppleToken(appleToken)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        Integer currentCoin = user.getHaveCoin();
        Integer coinsNeeded = calculateCoinsNeededForNextProduct(currentCoin);
        
        return UserCoinResponse.of(currentCoin, coinsNeeded);
    }
    
    /**
     * 다음 구매 가능한 상품까지 필요한 코인 계산
     */
    private Integer calculateCoinsNeededForNextProduct(Integer currentCoin) {
        Product nextProduct = productRepository.findFirstByCoinPriceGreaterThanOrderByCoinPriceAsc(currentCoin);
        
        if (nextProduct == null) {
            return 0; // 모든 상품을 구매할 수 있는 경우
        }
        
        return nextProduct.getCoinPrice() - currentCoin;
    }
}
