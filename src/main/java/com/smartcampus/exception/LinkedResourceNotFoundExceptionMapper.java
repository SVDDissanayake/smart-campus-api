package com.smartcampus.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public jakarta.ws.rs.core.Response toResponse(LinkedResourceNotFoundException exception) {
        return ErrorResponseFactory.build(
                422,
                "Unprocessable Entity",
                exception.getMessage(),
                uriInfo.getRequestUri().getPath());
    }
}
