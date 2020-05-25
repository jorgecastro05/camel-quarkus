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

        from("timer:helloDb?repeatCount=1").routeId("routeHelloWorldDb")
                .to("sql:{{example.sql.query}}?datasource=#db&outputType=StreamList")
                .split(body()).streaming().parallelProcessing()
                .bean(transformationComponent, "removeIdKey")
                .marshal().json(JsonLibrary.Jackson)
                .log("${body}")
                .end();

        from("kafka:my-topic?brokers=my-cluster-kafka-bootstrap:9092")
                .log("Message received from Kafka : ${body}")
                .log("    on the topic ${headers[kafka.TOPIC]}")
                .log("    on the partition ${headers[kafka.PARTITION]}")
                .log("    with the offset ${headers[kafka.OFFSET]}")
                .log("    with the key ${headers[kafka.KEY]}")
                .end();

    }
}
