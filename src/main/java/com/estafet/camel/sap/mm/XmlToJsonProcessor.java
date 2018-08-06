package com.estafet.camel.sap.mm;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONObject;
import org.json.XML;

public class XmlToJsonProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {
            // get the content
            String xml = exchange.getIn().getBody(String.class);

            // traform from xml to json
            byte[] encoded = xml.getBytes();
            JSONObject xmlJSONObj = XML.toJSONObject(new String(encoded));
            String json = xmlJSONObj.toString(4);

            // set the content
            exchange.getOut().setBody(json);
    }
}
