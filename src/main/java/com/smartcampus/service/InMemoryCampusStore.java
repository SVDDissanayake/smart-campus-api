package com.smartcampus.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

public final class InMemoryCampusStore {

    private static final InMemoryCampusStore INSTANCE = new InMemoryCampusStore();

    private final ConcurrentMap<String, Room> rooms = new ConcurrentHashMap<String, Room>();
    private final ConcurrentMap<String, Sensor> sensors = new ConcurrentHashMap<String, Sensor>();
    private final ConcurrentMap<String, CopyOnWriteArrayList<SensorReading>> readings =
            new ConcurrentHashMap<String, CopyOnWriteArrayList<SensorReading>>();

    private InMemoryCampusStore() {
    }

    public static InMemoryCampusStore getInstance() {
        return INSTANCE;
    }

    public ConcurrentMap<String, Room> getRooms() {
        return rooms;
    }

    public ConcurrentMap<String, Sensor> getSensors() {
        return sensors;
    }

    public ConcurrentMap<String, CopyOnWriteArrayList<SensorReading>> getReadings() {
        return readings;
    }
}
