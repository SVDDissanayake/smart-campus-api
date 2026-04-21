package com.smartcampus.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        return ErrorResponseFactory.build(
                Response.Status.INTERNAL_SERVER_ERROR,
                "An unexpected internal server error occurred.",
                uriInfo.getRequestUri().getPath());
    }
}
