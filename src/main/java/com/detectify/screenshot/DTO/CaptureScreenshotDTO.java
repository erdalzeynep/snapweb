package com.detectify.screenshot.DTO;

import com.detectify.screenshot.model.ScreenshotRequest;

import java.util.Map;

public class CaptureScreenshotDTO {

    private Long requestId;
    private Map<String, ScreenshotDTO> results;

    public CaptureScreenshotDTO() {
    }

    public CaptureScreenshotDTO(ScreenshotRequest req, Map<String,ScreenshotDTO> results) {
        this.requestId = req.getId();
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
