package com.onestep_be.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {
    
    private final String errorCode;
    private final HttpStatus httpStatus;
    
    public GlobalException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
    
    public GlobalException(String errorCode, String message) {
        this(errorCode, message, HttpStatus.BAD_REQUEST);
    }
    
    // 자주 사용되는 예외들
    public static GlobalException notFound(String resource) {
        return new GlobalException("NOT_FOUND", resource + "를 찾을 수 없습니다", HttpStatus.NOT_FOUND);
    }
    
    public static GlobalException badRequest(String message) {
        return new GlobalException("BAD_REQUEST", message, HttpStatus.BAD_REQUEST);
    }
    
    public static GlobalException unauthorized(String message) {
        return new GlobalException("UNAUTHORIZED", message, HttpStatus.UNAUTHORIZED);
    }
    
    public static GlobalException forbidden(String message) {
        return new GlobalException("FORBIDDEN", message, HttpStatus.FORBIDDEN);
    }
}
