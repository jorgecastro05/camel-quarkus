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

        from("activemq-component:queue:{{amq.queue}}").routeId("activemq_consumer")
                .log("Message received from Amq queue {{amq.queue}}: ${body}")
                .end();


        from("timer:helloWorld")
                .log("Sending message to amq")
                .transform(simple("Hello world {{example.secret.message}} ${id}"))
                .to("activemqproducer-component:{{amq.queue}}")
                .end();

    }
}
