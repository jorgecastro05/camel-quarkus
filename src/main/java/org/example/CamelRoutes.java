package org.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CamelRoutes extends RouteBuilder {

    @Inject
    protected TransformationComponent transformationComponent;

    @Override
    public void configure() throws Exception {
        getContext().setStreamCaching(true);
        getContext().setUseMDCLogging(true);

        from("timer:helloDb?period={{example.timer.period}}").routeId("routeHelloWorldDb")
                .log("Retrieving entries from database each {{example.timer.period}} milliseconds")
                .to("sql:{{example.sql.query}}?datasource=#db&outputType=StreamList")
                .split(body()).streaming().parallelProcessing()
                .bean(transformationComponent, "removeIdKey")
                .marshal().json(JsonLibrary.Jackson)
                .log("${body}")
                .end();

    }
}
