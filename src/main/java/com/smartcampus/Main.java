package com.smartcampus;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import com.smartcampus.config.AppConfig;

public class Main {

    private static final String SERVER_URI = "http://localhost:9090/";
    private static final String API_BASE_URI = "http://localhost:9090/api/v1/";

    private Main() {
    }

    public static HttpServer startServer() {
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(SERVER_URI), new AppConfig());
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = startServer();

        System.out.println("Smart Campus API is running at " + API_BASE_URI);
        System.out.println("Press Ctrl+C to stop the server.");

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        } finally {
            server.shutdownNow();
        }
    }
}
