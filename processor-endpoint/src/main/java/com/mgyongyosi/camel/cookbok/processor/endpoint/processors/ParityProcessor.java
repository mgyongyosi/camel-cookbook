package com.mgyongyosi.camel.cookbok.processor.endpoint.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ParityProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Number number = exchange.getIn().getBody(Number.class);
        String parity;

        if(number.intValue() % 2 == 0) {
           parity = "even";
        } else {
            parity = "odd";
        }

        exchange.getIn().setHeader("NumberParity", parity);
    }
}
