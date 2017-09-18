package com.mgyongyosi.camel.cookbook.custom.aggregator.routes;

import com.mgyongyosi.camel.cookbook.custom.aggregator.App;
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
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = App.class)
@UseAdviceWith
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SyncAggregatorRouteBuilderTest {

    @Autowired
    private CamelContext context;

    @EndpointInject(uri = "mock:direct:result")
    private MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    private ProducerTemplate template;

    @Test
    public void routeShouldSendSignal() throws Exception {

        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToString("To[direct:result]").replace().to(resultEndpoint);
            }
        });

        context.start();

        Map<String, Object> headersA1 = new HashMap<>();
        headersA1.put("system", "A");
        headersA1.put("deviceId", 1);

        Map<String, Object> headersB1 = new HashMap<>();
        headersB1.put("system", "B");
        headersB1.put("deviceId", 1);

        Map<String, Object> headersC2 = new HashMap<>();
        headersC2.put("system", "C");
        headersC2.put("deviceId", 2);

        Map<String, Object> headersC1 = new HashMap<>();
        headersC1.put("system", "C");
        headersC1.put("deviceId", 1);

        resultEndpoint.expectedMessageCount(2);

        template.sendBodyAndHeaders("", headersA1);
        template.sendBodyAndHeaders("", headersA1);
        template.sendBodyAndHeaders("", headersA1);
        template.sendBodyAndHeaders("", headersB1);
        template.sendBodyAndHeaders("", headersA1);
        template.sendBodyAndHeaders("", headersC2);
        template.sendBodyAndHeaders("", headersC1);

        template.sendBodyAndHeaders("", headersA1);
        template.sendBodyAndHeaders("", headersB1);
        template.sendBodyAndHeaders("", headersB1);
        template.sendBodyAndHeaders("", headersC1);

        resultEndpoint.assertIsSatisfied(10000);

        context.stop();
    }

    @Test
    public void routeShouldFailToSendSignal() throws Exception {

        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToString("To[direct:result]").replace().to(resultEndpoint);
            }
        });

        context.start();

        Map<String, Object> headersA1 = new HashMap<>();
        headersA1.put("system", "A");
        headersA1.put("deviceId", 1);

        Map<String, Object> headersB1 = new HashMap<>();
        headersB1.put("system", "B");
        headersB1.put("deviceId", 1);

        Map<String, Object> headersC2 = new HashMap<>();
        headersC2.put("system", "C");
        headersC2.put("deviceId", 2);

        Map<String, Object> headersC1 = new HashMap<>();
        headersC1.put("system", "C");
        headersC1.put("deviceId", 1);

        resultEndpoint.expectedMessageCount(0);

        template.sendBodyAndHeaders("", headersA1);
        template.sendBodyAndHeaders("", headersA1);
        template.sendBodyAndHeaders("", headersA1);
        template.sendBodyAndHeaders("", headersB1);
        template.sendBodyAndHeaders("", headersA1);
        template.sendBodyAndHeaders("", headersC2);

        resultEndpoint.assertIsSatisfied(10000);

        context.stop();
    }
}
