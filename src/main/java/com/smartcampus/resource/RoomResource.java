package com.smartcampus.resource;

import java.net.URI;
import java.util.List;

import com.smartcampus.model.CampusModels.Room;
import com.smartcampus.service.CampusService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Context;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private static final CampusService CAMPUS_SERVICE = CampusService.getInstance();

    @GET
    public List<Room> getRooms() {
        return CAMPUS_SERVICE.getRooms();
    }

    @POST
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        Room createdRoom = CAMPUS_SERVICE.createRoom(room);
        URI location = uriInfo.getAbsolutePathBuilder().path(createdRoom.getId()).build();

        return Response.created(location).entity(createdRoom).build();
    }

    @GET
    @Path("{roomId}")
    public Room getRoom(@PathParam("roomId") String roomId) {
        return CAMPUS_SERVICE.getRoom(roomId);
    }

    @DELETE
    @Path("{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        CAMPUS_SERVICE.deleteRoom(roomId);
        return Response.noContent().build();
    }
}
