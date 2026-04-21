package com.smartcampus.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ValidationException exception) {
        return ErrorResponseFactory.build(
                Response.Status.BAD_REQUEST,
                exception.getMessage(),
                uriInfo.getRequestUri().getPath());
    }
}
