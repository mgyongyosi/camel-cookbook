package com.mgyongyosi.camel.cookbook.custom.aggregator.routes;

import com.mgyongyosi.camel.cookbook.custom.aggregator.aggregators.SignalAggregationStrategy;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SyncAggregatorRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // @formatter:off

        from("direct:start")
            .log(LoggingLevel.INFO, "Received ${headers.system}${headers.deviceId}")
            .aggregate(header("deviceId"), new SignalAggregationStrategy(3))
            .log(LoggingLevel.INFO, "Signaled body: ${body}")
            .to("direct:result");

        // @formatter:on
    }
}
