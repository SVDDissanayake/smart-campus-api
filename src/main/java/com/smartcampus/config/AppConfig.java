package com.smartcampus.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1")
public class AppConfig extends ResourceConfig {

    public AppConfig() {
        packages("com.smartcampus");
        register(JacksonFeature.class);
        property(ServerProperties.WADL_FEATURE_DISABLE, true);
    }
}
