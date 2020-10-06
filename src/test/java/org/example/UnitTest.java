package org.example;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class UnitTest {

    @Inject
    CamelContext camelContext;

    @Inject
    ProducerTemplate producerTemplate;

    @Test
    public void test() throws Exception {
        AdviceWithRouteBuilder.adviceWith(camelContext, "route_consumer", route -> {
            route.replaceFromWith("direct:start");
        });
        AdviceWithRouteBuilder.adviceWith(camelContext, "route_producer", route -> {
            route.weaveById("producerEndpoint").replace().to("mock:result");
        });

        producerTemplate.sendBody("direct:start", "Hello World From test endpoint");
        MockEndpoint mockEndpoint = camelContext.getEndpoint("mock:result", MockEndpoint.class);
        mockEndpoint.expectedBodiesReceived("Hello World");
        mockEndpoint.assertIsSatisfied();
    }

}
