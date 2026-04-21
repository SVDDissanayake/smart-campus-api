package com.smartcampus.resource;

import java.net.URI;
import java.util.List;

import com.smartcampus.model.CampusModels.Sensor;
import com.smartcampus.model.CampusModels.SensorReading;
import com.smartcampus.service.CampusService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private static final CampusService CAMPUS_SERVICE = CampusService.getInstance();

    @GET
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        return CAMPUS_SERVICE.getSensors(type);
    }

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {
        Sensor createdSensor = CAMPUS_SERVICE.createSensor(sensor);
        URI location = uriInfo.getAbsolutePathBuilder().path(createdSensor.getId()).build();

        return Response.created(location).entity(createdSensor).build();
    }

    @GET
    @Path("{sensorId}")
    public Sensor getSensor(@PathParam("sensorId") String sensorId) {
        return CAMPUS_SERVICE.getSensor(sensorId);
    }

    @Path("{sensorId}/readings")
    public SensorReadingResource readings(@PathParam("sensorId") String sensorId) {
        CAMPUS_SERVICE.getSensor(sensorId);
        return new SensorReadingResource(sensorId);
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public static class SensorReadingResource {

        private static final CampusService CAMPUS_SERVICE = CampusService.getInstance();

        private final String sensorId;

        public SensorReadingResource(String sensorId) {
            this.sensorId = sensorId;
        }

        @GET
        public List<SensorReading> getReadings() {
            return CAMPUS_SERVICE.getSensorReadings(sensorId);
        }

        @POST
        public Response createReading(SensorReading reading, @Context UriInfo uriInfo) {
            SensorReading createdReading = CAMPUS_SERVICE.createSensorReading(sensorId, reading);
            URI location = uriInfo.getAbsolutePathBuilder().path(createdReading.getId()).build();

            return Response.created(location).entity(createdReading).build();
        }
    }
}
