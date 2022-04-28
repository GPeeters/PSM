package com.example.psm;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ReadXML {

    public static ArrayList<Instructie> ReadXML(String FILENAME){
        //-----READ XML FILE-----

        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // create arraylist of processes for later use
        ArrayList<Instructie> processes = new ArrayList<>();

        try {
            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(FILENAME));

            doc.getDocumentElement().normalize();

            System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
            System.out.println("------");

            // get <instruction>
            NodeList list = doc.getElementsByTagName("instruction");

            //System.out.println("ID  operation   address");

            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    String id = element.getElementsByTagName("processID").item(0).getTextContent();
                    String operation = element.getElementsByTagName("operation").item(0).getTextContent();
                    String address = element.getElementsByTagName("address").item(0).getTextContent();

                    // add to process list
                    processes.add(new Instructie(Integer.parseInt(id), operation, Integer.parseInt(address)));

                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return processes;
    }

}