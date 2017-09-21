package com.mgyongyosi.camel.cookbok.processor.endpoint.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class ParityCheckerRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // @formatter:off

        from("direct:start")
            .to("parity-check")
            .log("${body} is ${headers.NumberParity}.");

        // @formatter:on

    }
}
