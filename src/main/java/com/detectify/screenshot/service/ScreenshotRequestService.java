package com.detectify.screenshot.service;

import com.detectify.screenshot.model.ScreenshotRequest;
import com.detectify.screenshot.repository.ScreenshotRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScreenshotRequestService {

    @Autowired
    private ScreenshotRequestRepository requestRepository;

    public ScreenshotRequest add(ScreenshotRequest request){
        return requestRepository.save(request);
    }
}
