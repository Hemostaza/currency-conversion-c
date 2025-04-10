package org.hemostaza;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class XMLController {
    public void saveToXml(List<Item> addedItems,String xmlName) throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("faktura");
        doc.appendChild(root);

        for (Item item : addedItems) {

            Element record = doc.createElement("komputer");
            root.appendChild(record);

            Element name = doc.createElement("nazwa");
            name.appendChild(doc.createTextNode(item.getName()));
            record.appendChild(name);

            Element date = doc.createElement("data_ksiegowania");
            date.appendChild(doc.createTextNode(String.valueOf(item.getDate())));
            record.appendChild(date);

            Element usdValue = doc.createElement("koszt_USD");
            usdValue.appendChild(doc.createTextNode(String.valueOf(item.getUsd())));
            record.appendChild(usdValue);

            Element plnValue = doc.createElement("koszt_PLN");
            plnValue.appendChild(doc.createTextNode(String.valueOf(item.getPln())));
            record.appendChild(plnValue);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        String fileName = xmlName + ".xml";
        DOMSource source = new DOMSource(doc);
        StreamResult file = new StreamResult(new File(fileName));

        transformer.transform(source, file);
        System.out.println("Zapisano dane do pliku: " + fileName);
    }

    public List<Item> getFromXml(String xmlName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(new File(xmlName));

        doc.getDocumentElement().normalize();

        List<Item> itemsList = new ArrayList<>();

        NodeList list = doc.getElementsByTagName("komputer");

        for (int temp = 0; temp < list.getLength(); temp++) {

            Node node = list.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                // get text
                String name = element.getElementsByTagName("nazwa").item(0).getTextContent();
                String date = element.getElementsByTagName("data_ksiegowania").item(0).getTextContent();
                String usd = element.getElementsByTagName("koszt_USD").item(0).getTextContent();
                String pln = element.getElementsByTagName("koszt_PLN").item(0).getTextContent();

                if(pln.isBlank()){
                    pln = "0";
                }

                Item item = new Item(name, Date.valueOf(date),Double.parseDouble(usd),Double.parseDouble(pln));
                itemsList.add(item);

            }
        }
        return itemsList;
    }
}

