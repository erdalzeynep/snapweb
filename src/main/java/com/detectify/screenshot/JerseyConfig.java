package com.detectify.screenshot;

import com.detectify.screenshot.controller.ScreenshotController;
import com.detectify.screenshot.exception.EmptyUrlListExceptionMapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(ScreenshotController.class);
        register(EmptyUrlListExceptionMapper.class);

        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    }
}
