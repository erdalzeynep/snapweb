package com.detectify.screenshot.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

@Provider
public class EmptyUrlListExceptionMapper implements ExceptionMapper<EmptyUrlListException> {

    @Override
    public Response toResponse(EmptyUrlListException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("message", exception.getMessage());
        return Response.status(400).entity(response)
                .type(APPLICATION_JSON_TYPE).build();
    }
}
