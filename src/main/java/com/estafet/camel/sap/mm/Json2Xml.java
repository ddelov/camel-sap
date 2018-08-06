package com.estafet.camel.sap.mm;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.io.File;

public class Json2Xml {

    public static void main(String[] args) throws Exception {

        // create a camel context
        CamelContext context = new DefaultCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {

                @Override
                public void configure() throws Exception {
//                    from("file://home/ddelov/IdeaProjects/trtt/src/main/resources/json/?fileName=employee.json")
                    from("direct:start")
                            .process(new JsonToXmlProcessor ())
                            .to("seda:end");
                }
            });

            // start the context
            context.start();


//            String xml = "<employee>\r\n" + " <id>101</id>\r\n" + " <name>Dinesh Krishnan</name>\r\n"
//                    + " <age>20</age>\r\n" + "</employee>";
//
//            System.out.println(xml);

            String json = "{\"employee\": {\n" +
                    "  \"name\": \"Dinesh Krishnan\",\n" +
                    "  \"id\": 101,\n" +
                    "  \"age\": 20\n" +
                    "}}";

            // creating the producer template
            ProducerTemplate producerTemplate = context.createProducerTemplate();
            producerTemplate.sendBody("direct:start", json);

            // creating the consumer template
            ConsumerTemplate consumerTemplate = context.createConsumerTemplate();
            String xml = consumerTemplate.receiveBody("seda:end", String.class);

            System.out.println("------------JSON to XML-------------");
            System.out.println(xml);

            // stop the context
            context.stop();

        } catch (Exception e) {
            context.stop();
            e.printStackTrace();
        }

    }
}