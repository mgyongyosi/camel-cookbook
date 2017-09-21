package com.mgyongyosi.camel.cookbok.processor.endpoint;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = App.class)
@UseAdviceWith
public class ParityCheckerRouteBuilderTests {

    @Autowired
    private CamelContext context;

    @EndpointInject(uri = "mock:direct:result")
    private MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    private ProducerTemplate template;

    @Test
    public void routeShouldReturnOddFor1EvenFor6() throws Exception {

        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddLast().setBody(simple("${body} is ${headers.NumberParity}.")).to(resultEndpoint);
            }
        });

        context.start();

        resultEndpoint.expectedMessageCount(2);
        resultEndpoint.expectedBodiesReceived("1 is odd.", "6 is even.");

        template.sendBody(1);
        template.sendBody(6);

        resultEndpoint.assertIsSatisfied();

        context.stop();
    }

}
