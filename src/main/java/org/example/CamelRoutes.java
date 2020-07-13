package org.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestParamType;
import org.example.domain.Greeting;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CamelRoutes extends RouteBuilder {

    @Inject
    protected TransformationComponent transformationComponent;

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .apiProperty("api.title", "Greetings Api")
                .apiProperty("api.version", "1.0.0");

        rest("/").id("RootPage")
                .get().produces("text/plain")
                .route().transform(constant("Camel is Running!"));

        rest("/say")
                .post().id("OperationSay").description("Send a message").outType(Greeting.class)
                .consumes("text/plain").produces("application/json")
                .param().type(RestParamType.body).name("message").description("The messsage to send").dataType("string").endParam()
                .to("direct:say")
                .get("/hello").id("OperationHello").outType(Greeting.class).produces("application/json")
                .to("direct:hello")
                .get("/bye").id("OperationBye").outType(Greeting.class).produces("application/json")
                .to("direct:bye")
                .get("/secret").id("OperationSecret").produces("text/plain")
                .to("direct:secret");

        from("direct:say").routeId("routeSayMessage")
                .bean(transformationComponent, "sayMessage")
                .marshal().json(JsonLibrary.Jackson)
                .end();

        from("direct:hello").routeId("routeSayHello")
                .bean(transformationComponent, "sayHello")
                .marshal().json(JsonLibrary.Jackson)
                .end();

        from("direct:bye").routeId("routeSayBye")
                .bean(transformationComponent, "sayBye")
                .marshal().json(JsonLibrary.Jackson)
                .end();

        from("direct:secret").routeId("routeSaySecret")
                .transform().simple("{{example.secret.message}}")
                .end();


    }
}
