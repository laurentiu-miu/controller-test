package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
//import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

/**
 * Created by LaurentiuM on 2019-05-15.
 */
@Component
@ApplicationPath("/jersey")
public class JerseyConfiguration extends ResourceConfig {
    @Autowired
    ObjectMapper objectMapper;

    /*@Autowired
    public JerseyConfiguration() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Path.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Provider.class));
        provider.findCandidateComponents("com.example.demo").forEach(beanDefinition -> {
            try {
                register(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                System.out.println(e.getCause());
            }
        });
        JacksonJaxbJsonProvider provider2 = new JacksonJaxbJsonProvider();
        provider2.setMapper(objectMapper);
        register(provider2);
    }*/

    @Autowired
    public JerseyConfiguration(CorsResponseFilter responseFilter) {
        packages("com.example.demo");
        property(ServerProperties.WADL_FEATURE_DISABLE, true); // required for swagger

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // create JsonProvider to provide custom ObjectMapper
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(mapper);
        register(provider);
    }
}
