package com.smartcampus.exception;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(BadRequestException exception) {
        return ErrorResponseFactory.build(
                Response.Status.BAD_REQUEST,
                "Bad request. Please verify the JSON payload and request parameters.",
                uriInfo.getRequestUri().getPath());
    }
}
