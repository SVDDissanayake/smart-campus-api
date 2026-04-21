package com.smartcampus.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.smartcampus.exception.ResourceConflictException;
import com.smartcampus.exception.ResourceNotFoundException;
import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.exception.ValidationException;
import com.smartcampus.model.Room;

public class RoomService {

    private static final RoomService INSTANCE = new RoomService();

    private final InMemoryCampusStore store = InMemoryCampusStore.getInstance();

    private RoomService() {
    }

    public static RoomService getInstance() {
        return INSTANCE;
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<Room>();

        for (Room room : store.getRooms().values()) {
            rooms.add(new Room(room));
        }

        Collections.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room first, Room second) {
                return first.getId().compareToIgnoreCase(second.getId());
            }
        });

        return rooms;
    }

    public Room getRoomById(String roomId) {
        Room room = store.getRooms().get(roomId);

        if (room == null) {
            throw new ResourceNotFoundException("Room with id '" + roomId + "' was not found.");
        }

        return new Room(room);
    }

    public synchronized Room createRoom(Room room) {
        validateRoom(room);

        if (store.getRooms().containsKey(room.getId())) {
            throw new ResourceConflictException("Room with id '" + room.getId() + "' already exists.");
        }

        Room storedRoom = new Room(room);
        storedRoom.setSensorIds(new ArrayList<String>());
        store.getRooms().put(storedRoom.getId(), storedRoom);

        return new Room(storedRoom);
    }

    public synchronized void deleteRoom(String roomId) {
        Room room = store.getRooms().get(roomId);

        if (room == null) {
            throw new ResourceNotFoundException("Room with id '" + roomId + "' was not found.");
        }

        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                    "Room '" + roomId + "' cannot be deleted because sensors are still assigned to it.");
        }

        store.getRooms().remove(roomId);
    }

    public boolean roomExists(String roomId) {
        return store.getRooms().containsKey(roomId);
    }

    public synchronized void addSensorToRoom(String roomId, String sensorId) {
        Room room = store.getRooms().get(roomId);

        if (room == null) {
            throw new ResourceNotFoundException("Room with id '" + roomId + "' was not found.");
        }

        List<String> sensorIds = room.getSensorIds();
        if (!sensorIds.contains(sensorId)) {
            sensorIds.add(sensorId);
            room.setSensorIds(sensorIds);
        }
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

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
