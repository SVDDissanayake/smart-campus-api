package com.smartcampus.config;

import java.util.logging.Logger;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.smartcampus.exception.GlobalExceptionMapper;
import com.smartcampus.resource.DiscoveryResource;
import com.smartcampus.resource.RoomResource;
import com.smartcampus.resource.SensorResource;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;

@ApplicationPath("/api/v1")
public class AppConfig extends ResourceConfig {

    public AppConfig() {
        register(JacksonFeature.class);
        register(DiscoveryResource.class);
        register(RoomResource.class);
        register(SensorResource.class);
        register(GlobalExceptionMapper.class);
        register(ApiLoggingFilter.class);
        property(ServerProperties.WADL_FEATURE_DISABLE, true);
    }

    public static class ApiLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

        private static final Logger LOGGER = Logger.getLogger(ApiLoggingFilter.class.getName());

        @Override
        public void filter(ContainerRequestContext requestContext) {
            LOGGER.info("Incoming request: "
                    + requestContext.getMethod()
                    + " "
                    + requestContext.getUriInfo().getRequestUri());
        }

        @Override
        public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
            LOGGER.info("Outgoing response: "
                    + requestContext.getMethod()
                    + " "
                    + requestContext.getUriInfo().getRequestUri()
                    + " -> "
                    + responseContext.getStatus());
        }
    }
}
