/**
 *  Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.kogito.serverless.demo;

import com.fasterxml.jackson.databind.JsonNode;
import io.cloudevents.json.Json;
import io.cloudevents.v1.CloudEventBuilder;
import io.cloudevents.v1.CloudEventImpl;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.UUID;

@Path("/neworder")
public class NewOrderResource {

    @Inject
    @Channel("out-neworder")
    Emitter<String> newOrderEmitter;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void emitNewOrder(JsonNode newOrder) {
        CloudEventImpl<JsonNode> newOrderEvent =
                CloudEventBuilder.<JsonNode>builder()
                        .withId(UUID.randomUUID().toString())
                        .withType("newOrderEvent")
                        .withSource(URI.create("http://localhost:8080"))
                        .withData(newOrder)
                        .build();

        newOrderEmitter.send(Json.encode(newOrderEvent));

    }
}
