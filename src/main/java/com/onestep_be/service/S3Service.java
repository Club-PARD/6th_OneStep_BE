package com.onestep_be.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    
    /**
     * S3에 이미지 업로드 (사용자별 폴더)
     */
    public String uploadImage(MultipartFile file, Long userId) {
        try {
            // 사용자별 폴더 + 파일명 생성
            String fileName = generateUserFileName(userId, file.getOriginalFilename());
            
            // 메타데이터 설정 (파일 정보)
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            
            // S3에 업로드
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName, 
                    fileName, 
                    file.getInputStream(), 
                    metadata
            );
            
            amazonS3.putObject(putObjectRequest);
            
            // 업로드된 파일의 URL 반환
            String imageUrl = amazonS3.getUrl(bucketName, fileName).toString();
            
            log.info("S3 이미지 업로드 성공: {}", imageUrl);
            return imageUrl;
            
        } catch (IOException e) {
            log.error("S3 이미지 업로드 실패", e);
            throw new RuntimeException("이미지 업로드에 실패했습니다", e);
        }
    }
    
    /**
     * S3에서 이미지 삭제
     */
    public void deleteImage(String imageUrl) {
        try {
            // URL에서 키 추출 (버킷명 이후 부분)
            String key = extractKeyFromUrl(imageUrl);
            amazonS3.deleteObject(bucketName, key);
            log.info("S3 이미지 삭제 성공: {}", imageUrl);
        } catch (Exception e) {
            log.error("S3 이미지 삭제 실패: {}", imageUrl, e);
        }
    }
    
    /**
     * URL에서 S3 키 추출
     */
    private String extractKeyFromUrl(String imageUrl) {
        // https://bucket-name.s3.region.amazonaws.com/key 형태에서 key 부분 추출
        String bucketUrl = "https://" + bucketName + ".s3.";
        if (imageUrl.startsWith(bucketUrl)) {
            int keyStartIndex = imageUrl.indexOf(".com/") + 5;
            return imageUrl.substring(keyStartIndex);
        }
        throw new RuntimeException("잘못된 S3 URL 형식: " + imageUrl);
    }
    
    /**
     * 사용자별 폴더 + 유니크한 파일명 생성
     */
    private String generateUserFileName(Long userId, String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return "users/" + userId + "/missions/" + UUID.randomUUID().toString() + extension;
    }
}
