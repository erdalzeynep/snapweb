package com.detectify.screenshot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Screenshot extends AbstractEntity {

    @Column
    private String pageUrl;
    @Column
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "screenshotRequest_id")
    private ScreenshotRequest screenshotRequest;

    public Screenshot() {
    }

    public Screenshot(String pageUrl, String filePath, ScreenshotRequest screenshotRequest) {
        this.pageUrl = pageUrl;
        this.filePath = filePath;
        this.screenshotRequest = screenshotRequest;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ScreenshotRequest getScreenshotRequest() {
        return screenshotRequest;
    }

    public void setScreenshotRequest(ScreenshotRequest screenshotRequest) {
        this.screenshotRequest = screenshotRequest;
    }
}
