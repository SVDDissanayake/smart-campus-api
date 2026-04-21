package com.smartcampus.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceConflictExceptionMapper implements ExceptionMapper<ResourceConflictException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ResourceConflictException exception) {
        return ErrorResponseFactory.build(
                Response.Status.CONFLICT,
                exception.getMessage(),
                uriInfo.getRequestUri().getPath());
    }
}
