package com.smartcampus.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.exception.ValidationException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

public class SensorReadingService {

    private static final SensorReadingService INSTANCE = new SensorReadingService();

    private final InMemoryCampusStore store = InMemoryCampusStore.getInstance();
    private final SensorService sensorService = SensorService.getInstance();

    private SensorReadingService() {
    }

    public static SensorReadingService getInstance() {
        return INSTANCE;
    }

    public List<SensorReading> getReadingsForSensor(String sensorId) {
        sensorService.requireStoredSensor(sensorId);

        List<SensorReading> readings = new ArrayList<SensorReading>();
        List<SensorReading> storedReadings = store.getReadings().get(sensorId);

        if (storedReadings != null) {
            for (SensorReading reading : storedReadings) {
                readings.add(new SensorReading(reading));
            }
        }

        Collections.sort(readings, new Comparator<SensorReading>() {
            @Override
            public int compare(SensorReading first, SensorReading second) {
                return Long.compare(first.getTimestamp(), second.getTimestamp());
            }
        });

        return readings;
    }

    public synchronized SensorReading createReading(String sensorId, SensorReading reading) {
        validateReading(reading);

        Sensor sensor = sensorService.requireStoredSensor(sensorId);

        if ("MAINTENANCE".equals(sensor.getStatus().toUpperCase(Locale.ENGLISH))) {
            throw new SensorUnavailableException(
                    "Sensor '" + sensorId + "' is in MAINTENANCE mode and cannot accept new readings.");
        }

        SensorReading storedReading = new SensorReading(reading);

        if (isBlank(storedReading.getId())) {
            storedReading.setId(UUID.randomUUID().toString());
        }

        if (storedReading.getTimestamp() <= 0) {
            storedReading.setTimestamp(System.currentTimeMillis());
        }

        CopyOnWriteArrayList<SensorReading> readings = store.getReadings().get(sensorId);
        if (readings == null) {
            readings = new CopyOnWriteArrayList<SensorReading>();
            store.getReadings().put(sensorId, readings);
        }
        readings.add(storedReading);

        sensor.setCurrentValue(storedReading.getValue());

        return new SensorReading(storedReading);
    }

    private void validateReading(SensorReading reading) {
        if (reading == null) {
            throw new ValidationException("Sensor reading payload must not be empty.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
