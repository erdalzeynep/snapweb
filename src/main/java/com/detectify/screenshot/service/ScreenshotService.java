package com.detectify.screenshot.service;

import com.detectify.screenshot.model.Screenshot;
import com.detectify.screenshot.model.ScreenshotRequest;
import com.detectify.screenshot.repository.ScreenshotRepository;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScreenshotService {

    private final PhantomJSDriver webDriver;
    private final ScreenshotRepository repository;

    @Value(value = "${screenshot-output-path}")
    private String screenshotOutputPath;

    @Autowired
    public ScreenshotService(PhantomJSDriver webDriver, ScreenshotRepository repository) {
        this.webDriver = webDriver;
        this.repository = repository;
    }

    public List<Screenshot> addScreenshots(List<Screenshot> screenshots) {
        return repository.saveAll(screenshots);
    }

    public List<Screenshot> createScreenshots(ScreenshotRequest request, List<String> urlList) throws IOException {
        List<Screenshot> screenshotList = new ArrayList<>();
        for (String url : urlList) {
            String path = savePicture(url);
            screenshotList.add(new Screenshot(url, path, request));
        }
        return screenshotList;
    }

    private String savePicture(String url) throws IOException {

        webDriver.get(url);

        File srcFile = webDriver.getScreenshotAs(OutputType.FILE);

        File destFile = new File(screenshotOutputPath);
        FileUtils.moveFileToDirectory(srcFile, destFile, false);

        webDriver.manage().deleteAllCookies();

        return Paths.get(destFile.getPath(), srcFile.getName()).toString();
    }
}
