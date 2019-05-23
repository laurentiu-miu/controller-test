package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by LaurentiuM on 2019-05-15.
 */
//@RestController
//@Scope("request")
public class Spring {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MyService myService;

    /*@GetMapping(value = "/spring/getInts", produces = {MediaType.APPLICATION_JSON_VALUE})
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
    }*/
}
