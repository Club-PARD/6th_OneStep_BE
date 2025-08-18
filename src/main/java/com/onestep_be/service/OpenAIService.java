package com.onestep_be.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    
    public OpenAIService() {
        this.restTemplate = new RestTemplate();
        // HTTP 연결 설정 최적화
        System.setProperty("http.keepAlive", "false");
        System.setProperty("http.maxConnections", "5");
    }
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    /**
     * GPT로 이미지 검증
     */
    public boolean verifyImageWithGPT(String missionTitle, String imageUrl) {
        try {
            String prompt = createVerificationPrompt(missionTitle);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                    Map.of(
                        "role", "user",
                        "content", List.of(
                            Map.of("type", "text", "text", prompt),
                            Map.of("type", "image_url", "image_url", Map.of("url", imageUrl))
                        )
                    )
                ),
                "max_tokens", 10
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_API_URL, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String gptResponse = extractGPTResponse(response.getBody());
                boolean result = parseVerificationResult(gptResponse);
                
                log.info("GPT 이미지 검증 결과: 미션='{}', 결과={}, GPT응답='{}'", 
                        missionTitle, result, gptResponse);
                
                return result;
            }
            
            log.error("OpenAI API 호출 실패: {}", response.getStatusCode());
            return false;
            
        } catch (Exception e) {
            log.error("GPT 이미지 검증 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 미션 검증 프롬프트 생성
     */
    private String createVerificationPrompt(String missionTitle) {
        return String.format(
            "미션: '%s'\n\n" +
            "제출된 이미지를 분석하여 다음 기준으로 판단해주세요:\n" +
            "1. 이미지가 미션 제목에서 요구하는 주요 객체나 상황을 포함하고 있는가?\n" +
            "2. 이미지 품질이 미션 인증에 적합한가? (너무 흐리거나 어둡지 않음)\n\n" +
            "판단 결과:\n" +
            "- 위 2가지 조건을 모두 만족하면: true\n" +
            "- 하나라도 만족하지 않으면: false\n\n" +
            "답변은 반드시 'true' 또는 'false'로만 해주세요.",
            missionTitle
        );
    }

    /**
     * GPT 응답에서 텍스트 추출
     */
    private String extractGPTResponse(Map<String, Object> responseBody) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            }
        } catch (Exception e) {
            log.error("GPT 응답 파싱 오류: {}", e.getMessage());
        }
        return "";
    }

    /**
     * GPT 응답을 boolean으로 변환
     */
    private boolean parseVerificationResult(String gptResponse) {
        if (gptResponse == null) return false;
        
        String response = gptResponse.trim().toLowerCase();
        return response.contains("true") || response.equals("yes") || response.equals("맞음");
    }
}
