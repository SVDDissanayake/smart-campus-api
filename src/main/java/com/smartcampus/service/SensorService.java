package com.smartcampus.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.exception.ResourceConflictException;
import com.smartcampus.exception.ResourceNotFoundException;
import com.smartcampus.exception.ValidationException;
import com.smartcampus.model.Sensor;

public class SensorService {

    private static final SensorService INSTANCE = new SensorService();

    private final InMemoryCampusStore store = InMemoryCampusStore.getInstance();
    private final RoomService roomService = RoomService.getInstance();

    private SensorService() {
    }

    public static SensorService getInstance() {
        return INSTANCE;
    }

    public List<Sensor> getSensors(String type) {
        List<Sensor> sensors = new ArrayList<Sensor>();
        String normalizedType = normalize(type);

        for (Sensor sensor : store.getSensors().values()) {
            if (normalizedType == null || normalizedType.equals(normalize(sensor.getType()))) {
                sensors.add(new Sensor(sensor));
            }
        }

        Collections.sort(sensors, new Comparator<Sensor>() {
            @Override
            public int compare(Sensor first, Sensor second) {
                return first.getId().compareToIgnoreCase(second.getId());
            }
        });

        return sensors;
    }

    public Sensor getSensorById(String sensorId) {
        return new Sensor(requireStoredSensor(sensorId));
    }

    public synchronized Sensor createSensor(Sensor sensor) {
        validateSensor(sensor);

        if (!roomService.roomExists(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                    "Room with id '" + sensor.getRoomId() + "' does not exist for this sensor.");
        }

        if (store.getSensors().containsKey(sensor.getId())) {
            throw new ResourceConflictException("Sensor with id '" + sensor.getId() + "' already exists.");
        }

        Sensor storedSensor = new Sensor(sensor);
        storedSensor.setStatus(normalizeStatus(sensor.getStatus()));
        store.getSensors().put(storedSensor.getId(), storedSensor);
        roomService.addSensorToRoom(storedSensor.getRoomId(), storedSensor.getId());

        return new Sensor(storedSensor);
    }

    Sensor requireStoredSensor(String sensorId) {
        Sensor sensor = store.getSensors().get(sensorId);

        if (sensor == null) {
            throw new ResourceNotFoundException("Sensor with id '" + sensorId + "' was not found.");
        }

        return sensor;
    }

    private void validateSensor(Sensor sensor) {
        if (sensor == null) {
            throw new ValidationException("Sensor payload must not be empty.");
        }

        if (isBlank(sensor.getId())) {
            throw new ValidationException("Sensor id is required.");
        }

        if (isBlank(sensor.getType())) {
            throw new ValidationException("Sensor type is required.");
        }

        if (isBlank(sensor.getRoomId())) {
            throw new ValidationException("Sensor roomId is required.");
        }
    }

    private String normalizeStatus(String status) {
        if (isBlank(status)) {
            return "ACTIVE";
        }

        return status.trim().toUpperCase(Locale.ENGLISH);
    }

    private String normalize(String value) {
        if (isBlank(value)) {
            return null;
        }

        return value.trim().toUpperCase(Locale.ENGLISH);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
