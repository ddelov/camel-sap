package com.estafet.camel.sap.mm;

import org.apache.camel.*;
import org.json.JSONObject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.json.XML;

public class Xml2Json {

    public static void main(String[] args) throws Exception {

        // create a camel context
        CamelContext context = new DefaultCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {

                @Override
                public void configure() throws Exception {

                    from("direct:start")
                            .process(new XmlToJsonProcessor())
                            .to("seda:end");
                }
            });

            // start the context
            context.start();


            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><employee>\r\n" + " <id>101</id>\r\n" + " <name>Dinesh Krishnan</name>\r\n"
                    + " <age>20</age>\r\n" + "</employee>";

            System.out.println(xml);

            // creating the producer template
            ProducerTemplate producerTemplate = context.createProducerTemplate();
            producerTemplate.sendBody("direct:start", xml);

            // creating the consumer template
            ConsumerTemplate consumerTemplate = context.createConsumerTemplate();
            String json = consumerTemplate.receiveBody("seda:end", String.class);

            System.out.println("------------XML to JSON-------------");
            System.out.println(json);

            // stop the context
            context.stop();

        } catch (Exception e) {
            context.stop();
            e.printStackTrace();
        }

    }
}