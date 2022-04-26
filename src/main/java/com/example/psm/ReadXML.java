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

    private static final String FILENAME1 = "Instructions_30_3.xml";
    private static final String FILENAME2 = "Instructions_20000_4.xml";
    private static final String FILENAME3 = "Instructions_20000_20.xml";

//    public static void main(String[] args) {
//        // First read in all xml files;
//        ArrayList<Instructie> pro_30_3 = ReadXML(FILENAME1);
//        ArrayList<Instructie> pro_20000_4 = ReadXML(FILENAME2);
//        ArrayList<Instructie> pro_20000_20 = ReadXML(FILENAME3);
//
//        //-------INIT GUI-------
//        // new GUI(pro_30_3, pro_20000_4, pro_20000_20);
//    }

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

                    /*if (operation.equals("Start") || operation.equals("Write")){
                        System.out.println("" + id +"   "+ operation +"       "+ address);
                    }
                    else if (operation.equals("Read")){
                        System.out.println("" + id +"   "+ operation +"        "+ address);
                    }
                    else if (operation.equals("Terminate")){
                        System.out.println("" + id +"   "+ operation +"   "+ address);
                    }
                    else{
                        System.out.println("Error, operation unknown");
                    }*/
                }

            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return processes;
    }

}