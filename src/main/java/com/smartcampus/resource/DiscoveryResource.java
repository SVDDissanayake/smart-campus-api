package com.smartcampus.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Map<String, Object> discover() {
        Map<String, Object> response = new LinkedHashMap<String, Object>();

        response.put("version", "v1");
        response.put("rooms", "/api/v1/rooms");
        response.put("sensors", "/api/v1/sensors");

        return response;
    }
}
