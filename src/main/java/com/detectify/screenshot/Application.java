package com.detectify.screenshot;


import com.detectify.screenshot.controller.ScreenshotController;
import net.anthavio.phanbedder.Phanbedder;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;

@SpringBootApplication(scanBasePackageClasses = {ScreenshotController.class, Application.class})
public class Application {

    @Value(value = "${screenshot-output-path}")
    private String screenshotOutputPath;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PhantomJSDriver webDriver() {
        File phantomjs = Phanbedder.unpack();
        DesiredCapabilities dcaps = new DesiredCapabilities();
        dcaps.setCapability("takesScreenshot", true);
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjs.getAbsolutePath());
        return new PhantomJSDriver(dcaps);
    }

    @PostConstruct
    public void onStart(){
        File destFile = new File(screenshotOutputPath);
        if (!destFile.exists()) {
            destFile.mkdir();
        }
    }

    @PreDestroy
    public void onExit() {
        webDriver().close();
        webDriver().quit();
    }
}
