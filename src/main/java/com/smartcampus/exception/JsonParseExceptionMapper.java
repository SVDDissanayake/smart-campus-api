package com.smartcampus.exception;

import com.fasterxml.jackson.core.JsonParseException;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(JsonParseException exception) {
        return ErrorResponseFactory.build(
                Response.Status.BAD_REQUEST,
                "Invalid JSON payload. Please check the request body format.",
                uriInfo.getRequestUri().getPath());
    }
}
