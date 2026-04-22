package com.smartcampus.exception;

import com.smartcampus.model.CampusModels.ApiError;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof ApiExceptions.RoomNotEmptyException) {
            return build(Response.Status.CONFLICT, exception.getMessage());
        }

        if (exception instanceof ApiExceptions.LinkedResourceNotFoundException) {
            return build(422, "Unprocessable Entity", exception.getMessage());
        }

        if (exception instanceof ApiExceptions.SensorUnavailableException) {
            return build(Response.Status.FORBIDDEN, exception.getMessage());
        }

        if (exception instanceof ApiExceptions.ResourceNotFoundException) {
            return build(Response.Status.NOT_FOUND, exception.getMessage());
        }

        if (exception instanceof ApiExceptions.ResourceConflictException) {
            return build(Response.Status.CONFLICT, exception.getMessage());
        }

        if (exception instanceof ApiExceptions.ValidationException) {
            return build(Response.Status.BAD_REQUEST, exception.getMessage());
        }

        if (exception instanceof NotSupportedException) {
            return build(Response.Status.UNSUPPORTED_MEDIA_TYPE,
                    "Unsupported media type. This endpoint only accepts application/json.");
        }

        if (exception instanceof NotAllowedException) {
            return build(Response.Status.METHOD_NOT_ALLOWED, "HTTP method not allowed for this resource.");
        }

        if (exception instanceof BadRequestException) {
            return build(Response.Status.BAD_REQUEST,
                    "Bad request. Please verify the JSON payload and request parameters.");
        }

        if (exception instanceof WebApplicationException) {
            Response.StatusType statusInfo = ((WebApplicationException) exception).getResponse().getStatusInfo();
            String message = exception.getMessage() == null || exception.getMessage().trim().isEmpty()
                    ? statusInfo.getReasonPhrase()
                    : exception.getMessage();
            return build(statusInfo.getStatusCode(), statusInfo.getReasonPhrase(), message);
        }

        return build(Response.Status.INTERNAL_SERVER_ERROR, "An unexpected internal server error occurred.");
    }

    private Response build(Response.Status status, String message) {
        return build(status.getStatusCode(), status.getReasonPhrase(), message);
    }

    private Response build(int statusCode, String error, String message) {
        ApiError apiError = new ApiError(
                System.currentTimeMillis(),
                statusCode,
                error,
                message,
                getPath());

        return Response.status(statusCode)
                .type(MediaType.APPLICATION_JSON)
                .entity(apiError)
                .build();
    }

    private String getPath() {
        return uriInfo == null ? "unknown" : uriInfo.getRequestUri().getPath();
    }
}
