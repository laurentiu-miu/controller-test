package com.example.demo;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Created by LaurentiuM on 2019-05-15.
 */
@RestController
@Scope("request")
public class Spring {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MyService myService;

    @GetMapping(value = "/spring/getInts", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StreamingResponseBody> getInts() {
        System.out.println("Spring Thread Name controller:"+Thread.currentThread().getName());
        StreamingResponseBody streamingOutput = output -> {
            System.out.println("Spring Thread Name stream:"+Thread.currentThread().getName());
            long start_s = System.currentTimeMillis();
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
            System.out.println("spring streaming time: "+(end_s-start_s));
        };
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON_UTF8).body(streamingOutput);
    }
}
