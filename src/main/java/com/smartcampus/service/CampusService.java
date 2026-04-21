package com.smartcampus.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.smartcampus.exception.ApiExceptions.LinkedResourceNotFoundException;
import com.smartcampus.exception.ApiExceptions.ResourceConflictException;
import com.smartcampus.exception.ApiExceptions.ResourceNotFoundException;
import com.smartcampus.exception.ApiExceptions.RoomNotEmptyException;
import com.smartcampus.exception.ApiExceptions.SensorUnavailableException;
import com.smartcampus.exception.ApiExceptions.ValidationException;
import com.smartcampus.model.CampusModels.Room;
import com.smartcampus.model.CampusModels.Sensor;
import com.smartcampus.model.CampusModels.SensorReading;

public final class CampusService {

    private static final CampusService INSTANCE = new CampusService();

    private final ConcurrentMap<String, Room> rooms = new ConcurrentHashMap<String, Room>();
    private final ConcurrentMap<String, Sensor> sensors = new ConcurrentHashMap<String, Sensor>();
    private final ConcurrentMap<String, CopyOnWriteArrayList<SensorReading>> readings =
            new ConcurrentHashMap<String, CopyOnWriteArrayList<SensorReading>>();

    private CampusService() {
    }

    public static CampusService getInstance() {
        return INSTANCE;
    }

    public List<Room> getRooms() {
        List<Room> roomList = new ArrayList<Room>();

        for (Room room : rooms.values()) {
            roomList.add(new Room(room));
        }

        Collections.sort(roomList, new Comparator<Room>() {
            @Override
            public int compare(Room first, Room second) {
                return first.getId().compareToIgnoreCase(second.getId());
            }
        });

        return roomList;
    }

    public Room getRoom(String roomId) {
        return new Room(requireRoom(roomId));
    }

    public synchronized Room createRoom(Room room) {
        validateRoom(room);

        if (rooms.containsKey(room.getId())) {
            throw new ResourceConflictException("Room with id '" + room.getId() + "' already exists.");
        }

        Room storedRoom = new Room(room);
        storedRoom.setSensorIds(new ArrayList<String>());
        rooms.put(storedRoom.getId(), storedRoom);

        return new Room(storedRoom);
    }

    public synchronized void deleteRoom(String roomId) {
        Room room = requireRoom(roomId);

        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                    "Room '" + roomId + "' cannot be deleted because sensors are still assigned to it.");
        }

        rooms.remove(roomId);
    }

    public List<Sensor> getSensors(String type) {
        List<Sensor> sensorList = new ArrayList<Sensor>();
        String normalizedType = normalize(type);

        for (Sensor sensor : sensors.values()) {
            if (normalizedType == null || normalizedType.equals(normalize(sensor.getType()))) {
                sensorList.add(new Sensor(sensor));
            }
        }

        Collections.sort(sensorList, new Comparator<Sensor>() {
            @Override
            public int compare(Sensor first, Sensor second) {
                return first.getId().compareToIgnoreCase(second.getId());
            }
        });

        return sensorList;
    }

    public Sensor getSensor(String sensorId) {
        return new Sensor(requireSensor(sensorId));
    }

    public synchronized Sensor createSensor(Sensor sensor) {
        validateSensor(sensor);

        if (!rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                    "Room with id '" + sensor.getRoomId() + "' does not exist for this sensor.");
        }

        if (sensors.containsKey(sensor.getId())) {
            throw new ResourceConflictException("Sensor with id '" + sensor.getId() + "' already exists.");
        }

        Sensor storedSensor = new Sensor(sensor);
        storedSensor.setStatus(normalizeStatus(sensor.getStatus()));
        sensors.put(storedSensor.getId(), storedSensor);

        Room room = requireRoom(storedSensor.getRoomId());
        List<String> sensorIds = room.getSensorIds();
        if (!sensorIds.contains(storedSensor.getId())) {
            sensorIds.add(storedSensor.getId());
            room.setSensorIds(sensorIds);
        }

        return new Sensor(storedSensor);
    }

    public List<SensorReading> getSensorReadings(String sensorId) {
        requireSensor(sensorId);

        List<SensorReading> readingList = new ArrayList<SensorReading>();
        List<SensorReading> storedReadings = readings.get(sensorId);

        if (storedReadings != null) {
            for (SensorReading reading : storedReadings) {
                readingList.add(new SensorReading(reading));
            }
        }

        Collections.sort(readingList, new Comparator<SensorReading>() {
            @Override
            public int compare(SensorReading first, SensorReading second) {
                return Long.compare(first.getTimestamp(), second.getTimestamp());
            }
        });

        return readingList;
    }

    public synchronized SensorReading createSensorReading(String sensorId, SensorReading reading) {
        validateReading(reading);

        Sensor sensor = requireSensor(sensorId);
        if ("MAINTENANCE".equals(normalizeStatus(sensor.getStatus()))) {
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

        CopyOnWriteArrayList<SensorReading> sensorReadings = readings.get(sensorId);
        if (sensorReadings == null) {
            sensorReadings = new CopyOnWriteArrayList<SensorReading>();
            readings.put(sensorId, sensorReadings);
        }
        sensorReadings.add(storedReading);

        sensor.setCurrentValue(storedReading.getValue());

        return new SensorReading(storedReading);
    }

    private Room requireRoom(String roomId) {
        Room room = rooms.get(roomId);

        if (room == null) {
            throw new ResourceNotFoundException("Room with id '" + roomId + "' was not found.");
        }

        return room;
    }

    private Sensor requireSensor(String sensorId) {
        Sensor sensor = sensors.get(sensorId);

        if (sensor == null) {
            throw new ResourceNotFoundException("Sensor with id '" + sensorId + "' was not found.");
        }

        return sensor;
    }

    private void validateRoom(Room room) {
        if (room == null) {
            throw new ValidationException("Room payload must not be empty.");
        }
        if (isBlank(room.getId())) {
            throw new ValidationException("Room id is required.");
        }
        if (isBlank(room.getName())) {
            throw new ValidationException("Room name is required.");
        }
        if (room.getCapacity() <= 0) {
            throw new ValidationException("Room capacity must be greater than zero.");
        }
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

    private void validateReading(SensorReading reading) {
        if (reading == null) {
            throw new ValidationException("Sensor reading payload must not be empty.");
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
