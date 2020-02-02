package com.detectify.screenshot;


import com.detectify.screenshot.controller.ScreenshotController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.File;

@SpringBootApplication(scanBasePackageClasses = {ScreenshotController.class, Application.class})
public class Application {

    @Value(value = "${screenshot-output-path}")
    private String screenshotOutputPath;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @PostConstruct
    public void onStart() {
        File destFile = new File(screenshotOutputPath);
        if (!destFile.exists()) {
            destFile.mkdir();
        }
    }
}
