package com.detectify.screenshot.dto;

import com.detectify.screenshot.model.Screenshot;

public class ScreenshotDTO {

    private String downloadUrl;
    private Long screenshotId;

    public ScreenshotDTO() {
    }

    public ScreenshotDTO(Screenshot screenshot) {

        this.downloadUrl = "http://localhost:9443/api/1.0/download/"+screenshot.getScreenshotRequest().getId()+"/"+screenshot.getId();
        this.screenshotId = screenshot.getId();
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Long getScreenshotId() {
        return screenshotId;
    }

    public void setScreenshotId(Long screenshotId) {
        this.screenshotId = screenshotId;
    }
}
