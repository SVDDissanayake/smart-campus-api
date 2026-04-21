package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

public final class CampusModels {

    private CampusModels() {
    }

    public static class ApiError {

        private long timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

        public ApiError() {
        }

        public ApiError(long timestamp, int status, String error, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class Room {

        private String id;
        private String name;
        private int capacity;
        private List<String> sensorIds = new ArrayList<String>();

        public Room() {
        }

        public Room(String id, String name, int capacity) {
            this.id = id;
            this.name = name;
            this.capacity = capacity;
        }

        public Room(Room other) {
            this(other.id, other.name, other.capacity);
            setSensorIds(other.sensorIds);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public List<String> getSensorIds() {
            return new ArrayList<String>(sensorIds);
        }

        public void setSensorIds(List<String> sensorIds) {
            this.sensorIds = sensorIds == null
                    ? new ArrayList<String>()
                    : new ArrayList<String>(sensorIds);
        }
    }

    public static class Sensor {

        private String id;
        private String type;
        private String status;
        private double currentValue;
        private String roomId;

        public Sensor() {
        }

        public Sensor(String id, String type, String status, double currentValue, String roomId) {
            this.id = id;
            this.type = type;
            this.status = status;
            this.currentValue = currentValue;
            this.roomId = roomId;
        }

        public Sensor(Sensor other) {
            this(other.id, other.type, other.status, other.currentValue, other.roomId);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getCurrentValue() {
            return currentValue;
        }

        public void setCurrentValue(double currentValue) {
            this.currentValue = currentValue;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }
    }

    public static class SensorReading {

        private String id;
        private long timestamp;
        private double value;

        public SensorReading() {
        }

        public SensorReading(String id, long timestamp, double value) {
            this.id = id;
            this.timestamp = timestamp;
            this.value = value;
        }

        public SensorReading(SensorReading other) {
            this(other.id, other.timestamp, other.value);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}
