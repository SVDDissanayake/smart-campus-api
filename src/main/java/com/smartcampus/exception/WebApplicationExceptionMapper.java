package com.smartcampus.exception;

import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotSupportedException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(WebApplicationException exception) {
        Response.StatusType statusInfo = exception.getResponse().getStatusInfo();
        String message = resolveMessage(exception, statusInfo);

        return ErrorResponseFactory.build(
                statusInfo.getStatusCode(),
                statusInfo.getReasonPhrase(),
                message,
                uriInfo.getRequestUri().getPath());
    }

    private String resolveMessage(WebApplicationException exception, Response.StatusType statusInfo) {
        if (exception instanceof NotSupportedException) {
            return "Unsupported media type. This endpoint only accepts application/json.";
        }

        if (exception instanceof NotAllowedException) {
            return "HTTP method not allowed for this resource.";
        }

        if (exception.getMessage() == null || exception.getMessage().trim().isEmpty()) {
            return statusInfo.getReasonPhrase();
        }

        return exception.getMessage();
    }
}
