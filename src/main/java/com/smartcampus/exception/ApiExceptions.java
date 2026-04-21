package com.smartcampus.exception;

public final class ApiExceptions {

    private ApiExceptions() {
    }

    public static class ResourceNotFoundException extends RuntimeException {

        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public static class ResourceConflictException extends RuntimeException {

        public ResourceConflictException(String message) {
            super(message);
        }
    }

    public static class ValidationException extends RuntimeException {

        public ValidationException(String message) {
            super(message);
        }
    }

    public static class RoomNotEmptyException extends RuntimeException {

        public RoomNotEmptyException(String message) {
            super(message);
        }
    }

    public static class LinkedResourceNotFoundException extends RuntimeException {

        public LinkedResourceNotFoundException(String message) {
            super(message);
        }
    }

    public static class SensorUnavailableException extends RuntimeException {

        public SensorUnavailableException(String message) {
            super(message);
        }
    }
}
