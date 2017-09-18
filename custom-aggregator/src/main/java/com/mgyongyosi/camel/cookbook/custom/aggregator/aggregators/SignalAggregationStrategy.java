package com.mgyongyosi.camel.cookbook.custom.aggregator.aggregators;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;

import java.util.List;

public class SignalAggregationStrategy extends GroupedExchangeAggregationStrategy implements Predicate {

    private int numberOfSystems;

    public SignalAggregationStrategy(int numberOfSystems) {
        this.numberOfSystems = numberOfSystems;
    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Exchange exchange = super.aggregate(oldExchange, newExchange);

        List<Exchange> aggregatedExchanges = exchange.getProperty("CamelGroupedExchange", List.class);

        // Complete aggregation if we have "numberOfSystems" (currently 3) different messages (where "system" headers are different)
        // https://github.com/apache/camel/blob/master/camel-core/src/main/docs/eips/aggregate-eip.adoc#completing-current-group-decided-from-the-aggregationstrategy
        if (numberOfSystems == aggregatedExchanges.stream().map(e -> e.getIn().getHeader("system", String.class)).distinct().count()) {
            exchange.setProperty(Exchange.AGGREGATION_COMPLETE_CURRENT_GROUP, true);
        }

        return exchange;
    }

    @Override
    public boolean matches(Exchange exchange) {
        // make it infinite (4th bullet point @ https://github.com/apache/camel/blob/master/camel-core/src/main/docs/eips/aggregate-eip.adoc#about-completion)
        return false;
    }
}
