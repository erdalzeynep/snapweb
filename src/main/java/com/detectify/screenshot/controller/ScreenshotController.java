package com.detectify.screenshot.controller;

import com.detectify.screenshot.DTO.CaptureScreenshotDTO;
import com.detectify.screenshot.DTO.ScreenshotDTO;
import com.detectify.screenshot.model.Screenshot;
import com.detectify.screenshot.model.ScreenshotRequest;
import com.detectify.screenshot.service.ScreenshotRequestService;
import com.detectify.screenshot.service.ScreenshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Path("/api/1.0")

public class ScreenshotController {

    private final ScreenshotRequestService requestService;
    private final ScreenshotService screenshotService;

    @Autowired
    public ScreenshotController(ScreenshotRequestService requestService, ScreenshotService screenshotService) {
        this.requestService = requestService;
        this.screenshotService = screenshotService;
    }

    @POST
    @Path("/captureScreenshots")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(propagation = Propagation.REQUIRED)
    public CaptureScreenshotDTO captureScreenshots(@RequestBody List<String> urlList) throws IOException {

        Map<String, ScreenshotDTO> screenshotDTOS = new HashMap<>();

        ScreenshotRequest request = new ScreenshotRequest();
        requestService.add(request);

        List<Screenshot> screenshots = screenshotService.createScreenshots(request, urlList);

        screenshotService.addScreenshots(screenshots);

        for (Screenshot screenshot : screenshots) {
            screenshotDTOS.put(screenshot.getPageUrl(), new ScreenshotDTO(screenshot));
        }
        return new CaptureScreenshotDTO(request, screenshotDTOS);
    }
}
