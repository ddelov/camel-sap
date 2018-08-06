package com.estafet.camel.sap.mm;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONObject;
import org.json.XML;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

public class JsonToXmlProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {
        // get the json
        String json = exchange.getIn().getBody(String.class);

        // transform json to xml
        JSONObject jsonObject = new JSONObject(json);
        String xml = XML.toString(jsonObject);


//        String xml = XML.toString(jso, "gptJsonXml");
        StreamSource source = new StreamSource(new StringReader(xml));
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
//        System.out.println("writer = " + writer.toString());

        // set the xml
        exchange.getOut().setBody(/*xml*/writer.toString());
    }
}
