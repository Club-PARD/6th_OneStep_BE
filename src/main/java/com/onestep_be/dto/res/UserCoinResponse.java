package com.onestep_be.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCoinResponse {
    
    private final Integer haveCoin;
    private final Integer coinsNeededForNextProduct;
    
    public static UserCoinResponse of(Integer haveCoin, Integer coinsNeededForNextProduct) {
        return UserCoinResponse.builder()
                .haveCoin(haveCoin)
                .coinsNeededForNextProduct(coinsNeededForNextProduct)
                .build();
    }
}
