package com.tohyama.accountbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class GeminiDto {
    
    @Data
    @AllArgsConstructor
    public static class GeminiProxyRequest {
        private String type;
        private Object summaryData;
        private String modelName;

        public GeminiProxyRequest(String type, Object summaryData) {
            this.type = type;
            this.summaryData = summaryData;
            this.modelName = null;
        }
    }
    
    @Data
    @AllArgsConstructor
    public static class GeminiProxyResponse {
        private String report;
    }
}
