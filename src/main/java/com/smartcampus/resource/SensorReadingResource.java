package com.smartcampus.resource;

import java.net.URI;
import java.util.List;

import com.smartcampus.model.SensorReading;
import com.smartcampus.service.SensorReadingService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private static final SensorReadingService SENSOR_READING_SERVICE = SensorReadingService.getInstance();

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getReadings() {
        return SENSOR_READING_SERVICE.getReadingsForSensor(sensorId);
    }

    @POST
    public Response createReading(SensorReading reading, @Context UriInfo uriInfo) {
        SensorReading createdReading = SENSOR_READING_SERVICE.createReading(sensorId, reading);
        URI location = uriInfo.getAbsolutePathBuilder().path(createdReading.getId()).build();

        return Response.created(location).entity(createdReading).build();
    }
}
