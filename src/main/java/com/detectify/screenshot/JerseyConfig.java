package com.detectify.screenshot;

import com.detectify.screenshot.controller.ScreenshotController;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages(ScreenshotController.class.getPackage().getName());

        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    }
}
