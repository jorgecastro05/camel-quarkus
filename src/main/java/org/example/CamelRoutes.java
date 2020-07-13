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

            rest("/say")
                .get("/hello").produces("application/json").to("direct:hello")
                .get("/bye").consumes("application/json").to("direct:bye");

            from("direct:hello")
                .transform().constant("{\"\"}");
            from("direct:bye")
                .transform().constant("Bye World");

    }
}
