package com.smartcampus.resource;

import java.net.URI;
import java.util.List;

import com.smartcampus.model.Sensor;
import com.smartcampus.service.SensorService;

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

    private static final SensorService SENSOR_SERVICE = SensorService.getInstance();

    @GET
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        return SENSOR_SERVICE.getSensors(type);
    }

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {
        Sensor createdSensor = SENSOR_SERVICE.createSensor(sensor);
        URI location = uriInfo.getAbsolutePathBuilder().path(createdSensor.getId()).build();

        return Response.created(location).entity(createdSensor).build();
    }

    @GET
    @Path("{sensorId}")
    public Sensor getSensor(@PathParam("sensorId") String sensorId) {
        return SENSOR_SERVICE.getSensorById(sensorId);
    }

    @Path("{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        SENSOR_SERVICE.getSensorById(sensorId);
        return new SensorReadingResource(sensorId);
    }
}
