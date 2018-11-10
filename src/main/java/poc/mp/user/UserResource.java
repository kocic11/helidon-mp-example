/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package poc.mp.user;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * A simple JAX-RS resource.
 *
 * The message is returned as a JSON object.
 */
@Path("/user")
@RequestScoped
public class UserResource {

    @Inject
    private UserRepo userRepo;

    public UserResource() {

    }

    /**
     * Return list of users matching the provided name.
     * 
     * @param name user name
     * @return {@link JsonArray}
     */
    @Path("/get/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getByName(@PathParam("name") final String name) {
        List<User> users = userRepo.getByName(name);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        users.forEach(user -> {
            JsonObject jsonObject = Json.createObjectBuilder().add("id", user.getId()).add("name", user.getName())
                    .add("age", user.getAge()).build();
            arrayBuilder.add(jsonObject);
        });
        return arrayBuilder.build();
    }

    /**
     * Return list of all users.
     * 
     * @param name user name
     * @return {@link JsonArray}
     */
    @Path("/getall")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getAll() {
        List<User> users = userRepo.getAll();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        users.forEach(user -> {
            JsonObject jsonObject = Json.createObjectBuilder().add("id", user.getId()).add("name", user.getName())
                    .add("age", user.getAge()).build();
            arrayBuilder.add(jsonObject);
        });
        return arrayBuilder.build();
    }

    /**
     * Add a user.
     * 
     * @param jsonObject the user
     * @return {@link Response}
     */
    @Path("/add")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(JsonObject jsonObject) {
        User user = new User();
        user.setName(getStringFromJson("name", jsonObject));
        user.setAge(getIntFromJson("age", jsonObject));
        String userId = userRepo.add(user);
        return Response.created(UriBuilder.fromResource(this.getClass()).path("id/" + userId).build()).build();
    }

    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        User user = userRepo.getById(id);
        return Response.ok(user).build();
    }

    @DELETE
    @Path("/deleteall")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAll() {
        userRepo.deleteAll();
        return Response.ok().build();
    }

    private static String getStringFromJson(String key, JsonObject json) {
        String returnedString = null;
        if (json.containsKey(key)) {
            JsonString value = json.getJsonString(key);
            if (value != null) {
                returnedString = value.getString();
            }
        }
        return returnedString;
    }

    private static Integer getIntFromJson(String key, JsonObject json) {
        Integer returnedValue = null;
        if (json.containsKey(key)) {
            JsonNumber value = json.getJsonNumber(key);
            if (value != null) {
                returnedValue = value.intValue();
            }
        }
        return returnedValue;
    }
}
