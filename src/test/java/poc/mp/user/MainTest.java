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

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.spi.CDI;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.server.Server;

class MainTest {
        private static Server server;

        public static void main(String[] args) throws Exception {
                MainTest test = new MainTest();
                startTheServer();
                test.add();
                
                test.testDeleteAll();
                test.testGetAll();
        }

        @BeforeAll
        public static void startTheServer() throws Exception {
                server = Main.startServer();
        }

        @Test
        void add() {

                User user = new User("Abc", 21);
                Client client = ClientBuilder.newClient();

                Response response = client.target(getConnectionString("/user/add")).request().put(Entity.json(user));
                Assertions.assertEquals(Response.Status.CREATED.toString(), response.getStatusInfo().toString(),
                                "PUT status code");
        }

        @Test
        void testGetByName() {

                User user = new User("Xyz", 21);
                List<User> expectedList = new ArrayList<>();
                expectedList.add(user);

                Client client = ClientBuilder.newClient();

                Response response = client.target(getConnectionString("/user/add")).request().put(Entity.json(user));
                Assertions.assertEquals(Response.Status.CREATED.toString(), response.getStatusInfo().toString(),
                                "PUT status code");

                JsonArray jsonArray = client.target(getConnectionString("/user/get/Xyz")).request().get(JsonArray.class);

                List<User> actualList = new ArrayList<>();

                jsonArray.forEach(jsonValue -> {
                        user.setId(jsonValue.asJsonObject().getString("id"));
                        user.setName(jsonValue.asJsonObject().getString("name"));
                        user.setAge(jsonValue.asJsonObject().getInt("age"));
                        actualList.add(user);
                });
                Assertions.assertArrayEquals(expectedList.toArray(), actualList.toArray(), "Compare objects");

        }

        @Test
        void testGetAll() {
                Client client = ClientBuilder.newClient();
                Response response = client.target(getConnectionString("/user/getall")).request().get();
                Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(), "GET All status");
        }

        @Test
        void testGetById() {
                User user = new User("Xyz", 21);

                Client client = ClientBuilder.newClient();

                Response response = client.target(getConnectionString("/user/add")).request().put(Entity.json(user));
                
                JsonObject jsonObject = client.target(getConnectionString(response.getLocation().getPath())).request()
                                .get(JsonObject.class);
                if (jsonObject != null && jsonObject.containsKey("id")) {
                        Assertions.assertEquals("0001", jsonObject.getString("id"), "Compare objects");
                } else {
                        Assertions.fail("User not found.");
                }
        }

        @Test
        void testDeleteAll() {
                Client client = ClientBuilder.newClient();
                Response response = client.target(getConnectionString("/user/deleteall")).request().delete();
                Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(), "Delete status code");
        }

        @AfterAll
        static void destroyClass() {
                CDI<Object> current = CDI.current();
                ((SeContainer) current).close();
        }

        private String getConnectionString(String path) {
                return "http://localhost:" + server.getPort() + path;
        }
}
