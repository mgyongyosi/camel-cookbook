package com.mgyongyosi.camel.cookbok.processor.endpoint.components;

import com.mgyongyosi.camel.cookbok.processor.endpoint.processors.ParityProcessor;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.ProcessorEndpoint;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ParityComponent extends DefaultComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return new ProcessorEndpoint(uri, this, new ParityProcessor());
    }
}