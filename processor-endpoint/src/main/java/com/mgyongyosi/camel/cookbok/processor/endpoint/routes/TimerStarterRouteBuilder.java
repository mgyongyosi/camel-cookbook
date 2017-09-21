package com.mgyongyosi.camel.cookbok.processor.endpoint.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TimerStarterRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // @formatter:off

        from("timer:start?period=5s")
            .setBody(simple("${random(100)}"))
            .to("direct:start");

        // @formatter:on

    }
}
