package org.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CamelRoutes extends RouteBuilder {

    @Inject
    private TransformationComponent transformationComponent;

    @Override
    public void configure() throws Exception {
        getContext().setStreamCaching(true);
        getContext().setUseMDCLogging(true);

        from("timer:hello?period=10000").routeId("routeHelloWorld")
                .to("sql:{{example.sql.query}}?datasource=#db")
                .split(body()).parallelProcessing()
                .bean(transformationComponent,"removeIdKey")
                .marshal().json(JsonLibrary.Jackson)
                .log("${body}")
                .end();
    }
}
