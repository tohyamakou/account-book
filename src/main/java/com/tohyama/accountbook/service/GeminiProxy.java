package com.tohyama.accountbook.service;

import com.tohyama.accountbook.dto.GeminiDto;

public interface GeminiProxy {
    GeminiDto.GeminiProxyResponse analyze(GeminiDto.GeminiProxyRequest request);
}
