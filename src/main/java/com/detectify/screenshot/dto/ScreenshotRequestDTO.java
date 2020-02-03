package com.detectify.screenshot.dto;

import java.util.Map;

public class ScreenshotRequestDTO {

    private Long requestId;
    private Map<String, ScreenshotDTO> results;

    public ScreenshotRequestDTO() {
    }

    public ScreenshotRequestDTO(Long requestId, Map<String, ScreenshotDTO> results) {
        this.requestId = requestId;
        this.results = results;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Map<String, ScreenshotDTO> getResults() {
        return results;
    }

    public void setResults(Map<String, ScreenshotDTO> results) {
        this.results = results;
    }
}
