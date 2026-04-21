package com.smartcampus.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Map<String, Object> discover() {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        Map<String, String> admin = new LinkedHashMap<String, String>();
        Map<String, String> resources = new LinkedHashMap<String, String>();

        admin.put("name", "Smart Campus Operations");
        admin.put("email", "smartcampus-admin@westminster.ac.uk");

        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");

        response.put("version", "v1");
        response.put("admin", admin);
        response.put("resources", resources);

        return response;
    }
}
