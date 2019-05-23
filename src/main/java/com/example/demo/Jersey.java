package com.example.demo;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Created by LaurentiuM on 2019-05-15.
 */
@Component
@Scope("request")
@Path("/getInts")
@Produces(MediaType.APPLICATION_JSON)
public class Jersey {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MyService myService;

    @GET
    public void getAll(@Suspended AsyncResponse response) {
        System.out.println("Jeresy Thread Name controller:"+Thread.currentThread().getName());
        try {

            StreamingOutput streamingOutput = output -> {
                long start_s = System.currentTimeMillis();
                System.out.println("Jeresy Thread Name stream:"+Thread.currentThread().getName());
                JsonGenerator jg = objectMapper.getFactory().createGenerator(output, JsonEncoding.UTF8);
                jg.writeStartObject();
                jg.writeStringField("entry", UUID.randomUUID().toString());
                jg.writeObjectField("delivery_date", ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());

                jg.writeFieldName("entries");
                jg.writeStartArray();

                myService.writeObject(jg);

                jg.writeEndArray();
                jg.writeEndObject();
                jg.flush();
                jg.close();
                long end_s = System.currentTimeMillis();
                System.out.println("jersey streaming time: "+(end_s-start_s));
            };

            response.resume(Response.ok(streamingOutput)
                    .type(MediaType.APPLICATION_JSON_TYPE.withCharset("UTF-8")).build());

        } finally {

        }
    }
}
