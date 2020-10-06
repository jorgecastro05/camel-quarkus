package org.example;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CamelRoutes extends RouteBuilder {

    @Inject
    protected TransformationComponent transformationComponent;

    @Override
    public void configure() throws Exception {

        from("activemq-component:queue:{{amq.queue}}").routeId("route_consumer")
                .log("Message received from Amq queue {{amq.queue}}: ${body}")
                .to("direct:transformationRoute")
                .end();


        from("timer:helloWorld")
                .log("Sending message to amq")
                .transform(simple("Hello world {{example.secret.message}} ${id}"))
                .to("activemqproducer-component:{{amq.queue}}")
                .end();

        from("direct:transformationRoute").routeId("route_transformation")
                .log("Transforming the message")
                .transform(simple("${body} - ${id}"))
                .to("direct:producerRoute")
                .end();

        from("direct:producerRoute").routeId("route_producer")
                .log("Consuming producer service")
                .to("direct:externalService").id("producerEndpoint")
                .end();

    }
}
