package com.detectify.screenshot.service;

import com.detectify.screenshot.model.Screenshot;
import com.detectify.screenshot.model.ScreenshotRequest;
import com.detectify.screenshot.repository.ScreenshotRepository;
import jdk.nashorn.internal.objects.AccessorPropertyDescriptor;
import net.anthavio.phanbedder.Phanbedder;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScreenshotService {

    private final ScreenshotRepository repository;

    private PhantomJSDriver webDriver;

    private final File phantomjs = Phanbedder.unpack();

    @Value(value = "${screenshot-output-path}")
    private String screenshotOutputPath;

    @Autowired
    public ScreenshotService(ScreenshotRepository repository) {
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
        if (null == webDriver || null == webDriver.getSessionId()) {
            webDriver = createFreshWebDriver();
        }

        webDriver.get(url);

        File srcFile = webDriver.getScreenshotAs(OutputType.FILE);

        File destFile = new File(screenshotOutputPath);
        FileUtils.moveFileToDirectory(srcFile, destFile, false);

        return Paths.get(destFile.getPath(), srcFile.getName()).toString();
    }

    private PhantomJSDriver createFreshWebDriver() {
        DesiredCapabilities dcaps = new DesiredCapabilities();
        dcaps.setCapability("takesScreenshot", true);
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjs.getAbsolutePath());
        return new PhantomJSDriver(dcaps);
    }

    public Screenshot getScreenshotByScreenshotIdAndRequestId(Long screenshotId, Long givenRequestId) {
        if (repository.existsById(screenshotId)) {
            Screenshot persistedScreenshot = repository.getOne(screenshotId);
            boolean isScreenshotIdMatchesRequestId = persistedScreenshot.getScreenshotRequest().getId().equals(givenRequestId);
            if (isScreenshotIdMatchesRequestId)
                return persistedScreenshot;
            else
                return null;
        } else
            return null;
    }

    @PostConstruct
    public void onDestroy() {
        if (null != webDriver) {
            webDriver.close();
            webDriver.quit();
        }
    }
}
