package com.example.demo;

import com.fasterxml.jackson.core.JsonGenerator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.IntStream;

/**
 * Created by LaurentiuM on 2019-05-15.
 */
@Service
public class MyService {
    public void writeObject(JsonGenerator jg){
        IntStream.rangeClosed(0,10000).forEach(i-> {
            try {
                jg.writeObject("entry:"+i);
                jg.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
