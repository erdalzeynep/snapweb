package com.detectify.screenshot.controller;

import com.detectify.screenshot.dto.ScreenshotRequestDTO;
import com.detectify.screenshot.dto.ScreenshotDTO;
import com.detectify.screenshot.exception.EmptyUrlListException;
import com.detectify.screenshot.exception.NotFoundException;
import com.detectify.screenshot.model.Screenshot;
import com.detectify.screenshot.model.ScreenshotRequest;
import com.detectify.screenshot.service.ScreenshotRequestService;
import com.detectify.screenshot.service.ScreenshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
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
    public ScreenshotRequestDTO captureScreenshots(@RequestBody List<String> urlList) throws IOException {

        Map<String, ScreenshotDTO> screenshotDTOS = new HashMap<>();

        if (urlList.size() == 0) {
            throw new EmptyUrlListException();
        } else {
            ScreenshotRequest request = new ScreenshotRequest();
            requestService.add(request);

            List<Screenshot> screenshots = screenshotService.createScreenshots(request, urlList);

            screenshotService.addScreenshots(screenshots);

            for (Screenshot screenshot : screenshots) {
                screenshotDTOS.put(screenshot.getPageUrl(), new ScreenshotDTO(screenshot));
            }
            return new ScreenshotRequestDTO(request.getId(), screenshotDTOS);
        }
    }

    @GET
    @Path("/download/{requestId}/{screenshotId}")
    @Produces("image/png")
    @Transactional(propagation = Propagation.REQUIRED)
    public Response download(@PathParam("requestId") Long requestId,
                             @PathParam("screenshotId") Long screenshotId) {

        Screenshot foundScreenshot = screenshotService.getScreenshotByScreenshotIdAndRequestId(screenshotId, requestId);
        if (foundScreenshot == null)
            throw new NotFoundException();
        else {
            String filePath = foundScreenshot.getFilePath();
            File file = new File(filePath);
            return Response.ok(file).build();
        }
    }

    @GET
    @Path("/fetchRequestDetail/{requestId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(propagation = Propagation.REQUIRED)
    public ScreenshotRequestDTO fetchRequestDetail(@PathParam("requestId") Long requestID) {
        Map<String, ScreenshotDTO> screenshotDTOS = new HashMap<>();
        List<Screenshot> screenshots = screenshotService.getScreenshotByScreenshotId(requestID);
        if (screenshots.size() != 0) {
            for (Screenshot screenshot : screenshots) {
                screenshotDTOS.put(screenshot.getPageUrl(), new ScreenshotDTO(screenshot));
            }
            return new ScreenshotRequestDTO(requestID, screenshotDTOS);
        } else
            throw new NotFoundException();
    }
}
