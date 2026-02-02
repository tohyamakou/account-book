package com.tohyama.accountbook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tohyama.accountbook.constants.consts;
import com.tohyama.accountbook.dto.GeminiDto;
import com.tohyama.accountbook.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiProxyImpl implements GeminiProxy {
    private static final Logger log = LoggerFactory.getLogger(GeminiProxyImpl.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${ai.prompt.daily:#{null}}")
    private String dailyPrompt;
    
    @Value("${ai.prompt.weekly:#{null}}")
    private String weeklyPrompt;
    
    @Value("${ai.prompt.monthly:#{null}}")
    private String monthlyPrompt;

    public GeminiProxyImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public GeminiDto.GeminiProxyResponse analyze(GeminiDto.GeminiProxyRequest request) {
        try {
            String summaryDataJson = objectMapper.writeValueAsString(request.getSummaryData());
            
            String selectedPrompt = selectPromptByType(request.getType());
            
            String promptText = selectedPrompt + "\n" +
                    "mode:  " + request.getType() + " data:\n" + summaryDataJson;
            
            String modelName = request.getModelName() != null ? request.getModelName() : consts.geminiFlash3;
            
            String url = String.format("https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                    modelName, apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> part = new HashMap<>();
            part.put("text", promptText);
            Map<String, Object> content = new HashMap<>();
            content.put("parts", List.of(part));
            requestBody.put("contents", List.of(content));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String result = extractTextFromResponse(response.getBody());
                return new GeminiDto.GeminiProxyResponse(result);
            } else {
                throw new RuntimeException("Gemini API 呼び出し失敗: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Gemini API 呼び出し失敗: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private String extractTextFromResponse(Map<String, Object> responseBody) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.getFirst().get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    return (String) parts.getFirst().get("text");
                }
            }
            return "失敗しました。";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    private String selectPromptByType(String type) {
        if (StringUtils.isEmpty(type)) {
            return dailyPrompt != null ? dailyPrompt : "";
        }
        
        String lowerType = type.toLowerCase();
        
        switch (lowerType) {
            case "daily" -> {
                if (!StringUtils.isEmpty(dailyPrompt)) {
                    return dailyPrompt;
                }
            }
            case "weekly" -> {
                if (!StringUtils.isEmpty(weeklyPrompt)) {
                    return weeklyPrompt;
                }
            }
            case "monthly" -> {
                if (!StringUtils.isEmpty(monthlyPrompt)) {
                    return monthlyPrompt;
                }
            }
        }
        return dailyPrompt != null ? dailyPrompt : "";
    }
}
