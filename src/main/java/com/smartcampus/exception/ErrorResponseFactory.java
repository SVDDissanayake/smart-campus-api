package com.smartcampus.exception;

import com.smartcampus.model.ApiError;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public final class ErrorResponseFactory {

    private ErrorResponseFactory() {
    }

    public static Response build(Response.Status status, String message, String path) {
        return build(status.getStatusCode(), status.getReasonPhrase(), message, path);
    }

    public static Response build(int statusCode, String error, String message, String path) {
        ApiError apiError = new ApiError(
                System.currentTimeMillis(),
                statusCode,
                error,
                message,
                path);

        return Response.status(statusCode)
                .type(MediaType.APPLICATION_JSON)
                .entity(apiError)
                .build();
    }
}
