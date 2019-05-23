package com.example.demo;


import org.apache.coyote.http11.Http11Nio2Protocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
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

    /*@Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

        factory.addErrorPages();

        factory.setProtocol(Http11Nio2Protocol.class.getName());

        factory.addConnectorCustomizers(connector -> {
            //disable lookups
            connector.setEnableLookups(false);

            //no maximum here
            connector.setMaxPostSize(maxPostSize);

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
    }*/


    @Bean
    public EmbeddedServletContainerFactory servletContainerFactory() {

        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory() {
            @Override
            public void addErrorPages(ErrorPage... errorPages) {
            }
        };

        factory.setProtocol(Http11Nio2Protocol.class.getName());

        factory.addConnectorCustomizers(connector -> {
            //https://tomcat.apache.org/tomcat-8.0-doc/config/http.html#Common_Attributes

            //disable lookups
            connector.setEnableLookups(false);

            //no maximum here
            connector.setMaxPostSize(maxPostSize);

            connector.setProperty("acceptorThreadCount", String.valueOf(acceptorThreadCount)); //cause we do not use keep alive yet we increase it to 2
            connector.setProperty("connectionTimeout", String.valueOf(connectionTimeout)); //in ms, 60000 is the default
            connector.setProperty("maxKeepAliveRequests", String.valueOf(maxKeepAliveRequests));  //1 -> disable keep alive

            Http11Nio2Protocol proto = ((Http11Nio2Protocol) connector.getProtocolHandler());

            //200 is default
            proto.setMaxThreads(maxThreads);

            //100 is default, this is to few and can cause connection resets.
            //This is nothing different from setting backlog on a server socket
            proto.setBacklog(backlog); //this is the same as acceptCount

            proto.setMaxConnections(maxConnections);

            System.out.println("maxKeepAliveRequests: {}"+ proto.getMaxKeepAliveRequests()); //default 100
            System.out.println("acceptorThreadCount (endpoint prop): {}"+ proto.getEndpoint().getProperty("acceptorThreadCount"));
            System.out.println("acceptorThreadCount (protocol): {}"+ proto.getProperty("acceptorThreadCount"));
            System.out.println("acceptorThreadCount (endpoint method): {}"+ proto.getEndpoint().getAcceptorThreadCount());
            System.out.println("acceptCount: {}"+ connector.getProperty("acceptCount")); //default 100
            System.out.println("maxThreads: {}"+ proto.getMaxThreads()); //default 200
            System.out.println("backlog (==acceptCount): {}"+ proto.getBacklog()); //default 100
            System.out.println("maxConnections: {}"+ proto.getMaxConnections()); //default 10000
            System.out.println("maxHeaderCount: {}"+ proto.getMaxHeaderCount()); //default 100
            System.out.println("maxHttpHeaderSize: {}"+ proto.getMaxHttpHeaderSize()); //default 8192
            System.out.println("maxPostSize: {}"+ connector.getMaxPostSize()); //2097152
            System.out.println("URIEncoding: {}"+ connector.getURIEncoding());
            System.out.println("connectionTimeout: {}"+ proto.getConnectionTimeout()); //60000

        });

        return factory;
    }
}
