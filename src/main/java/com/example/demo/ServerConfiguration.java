package com.example.demo;

import org.apache.coyote.http11.Http11Nio2Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by LaurentiuM on 2019-05-15.
 */
@Configuration
public class ServerConfiguration {
    private int maxPostSize = 8*1024*1024; //8mb

    private int acceptorThreadCount = 2;

    private int connectionTimeout = 15000; //15 sek

    private int maxKeepAliveRequests = 1;//1==keep alive disabled

    private int maxThreads = 100;

    private int backlog = 100;

    private int maxConnections = 20000;

    //@Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

        factory.addErrorPages();

        factory.setProtocol(Http11Nio2Protocol.class.getName());

        factory.addConnectorCustomizers(connector -> {
            //disable lookups
            connector.setEnableLookups(false);

            //no maximum here
           // connector.setMaxPostSize(maxPostSize);

            Http11Nio2Protocol proto = ((Http11Nio2Protocol) connector.getProtocolHandler());

            proto.setAcceptorThreadCount(acceptorThreadCount);
            proto.setConnectionTimeout(connectionTimeout);
            proto.setMaxKeepAliveRequests(maxKeepAliveRequests);

            //200 is default
            proto.setMaxThreads(maxThreads);

            //100 is default, this is to few and can cause connection resets.
            //This is nothing different from setting backlog on a server socket
            proto.setAcceptCount(backlog); //this is the same as acceptCount

            proto.setMaxConnections(maxConnections);

            System.out.println("maxKeepAliveRequests: "+ proto.getMaxKeepAliveRequests()); //default 100
            System.out.println("acceptorThreadCount (protocol): "+ proto.getAcceptorThreadCount());
            System.out.println("acceptCount: "+ connector.getProperty("acceptCount")); //default 100
            System.out.println("maxThreads: "+ proto.getMaxThreads()); //default 200
            System.out.println("backlog (==acceptCount): "+ proto.getAcceptCount()); //default 100
            System.out.println("maxConnections: "+ proto.getMaxConnections()); //default 10000
            System.out.println("maxHeaderCount: "+ proto.getMaxHeaderCount()); //default 100
            System.out.println("maxHttpHeaderSize: "+ proto.getMaxHttpHeaderSize()); //default 8192
            System.out.println("maxPostSize: "+ connector.getMaxPostSize()); //2097152
            System.out.println("URIEncoding: "+ connector.getURIEncoding());
            System.out.println("connectionTimeout: "+ proto.getConnectionTimeout()); //60000
        });

        return factory;
    }
}
