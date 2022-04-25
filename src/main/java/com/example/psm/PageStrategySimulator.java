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

public class PageStrategySimulator {

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

    static class Instructie{
        int pid;        //ProcessID
        String op;      //Operation
        int add;        //Address

        public Instructie(int id, String op, int add) {
            this.pid = id;
            this.op = op;
            this.add = add;
        }
    }

    static class Page{
        
        int PB;
        int MB;
        int LRU;
        int frameNum;

        public Page(int PB, int MB, int LRU, int frameNum) {
            this.PB = PB;
            this.MB = MB;
            this.LRU = LRU;
            this.frameNum = frameNum;
        }
        
    }

    static class Proces {
        int pid;
        static ArrayList<Page> pageTable = new ArrayList<>();
        public Proces(int pid, ArrayList<Proces> procList, int time){
            this.pid = pid;
            try{
                for (int i = 0; i < 12/(procList.size()+1); i++) {
                    pageTable.add(new Page(1,0, -1, getframe(procList)));
                }
                for (int i = 0; i < 16-(12/(procList.size()+1)); i++) {
                    pageTable.add(new Page(0, 0, -1, -1));
                }
            }
            catch (Exception e) {
                for (int i = 0; i < 12; i++) {
                    pageTable.add(new Page(1,0, -1, i));
                }
                for (int i = 0; i < 4; i++) {
                    pageTable.add(new Page(0, -1, -1, -1));
                }
            }

        }
        public int getframe(ArrayList<Proces> procList) {
            //schrijf hier methode om een frame te alloceren
        
            //get process with the most frames
            int temp_Aantalframes = -1;
            int temp_procID = -1;
            for (Proces proces : procList) {
                int aantalFrames = 0;
                for (Page page : proces.pageTable) {
                    if (page.PB == 1) {
                        aantalFrames = aantalFrames + 1;                        
                    }                    
                }
                if(aantalFrames > temp_Aantalframes){
                    temp_Aantalframes = aantalFrames;
                    temp_procID = proces.pid;
                }
            }

            //unload the page with the least accessed time
            int LRU_time = 100000000;
            int LRU_index = -1;
            for (Page p : procList.get(temp_procID).pageTable) {
                if(p.PB ==1 && p.LRU < LRU_time){
                    LRU_time = p.LRU;
                    LRU_index = procList.get(temp_procID).pageTable.indexOf(p);
                }                
            }
            Page p = procList.get(temp_procID).pageTable.get(LRU_index);
            p.PB =0;
            int allocatedframe = p.frameNum;
            p.frameNum =-1;

            //return the number of the allocated frame
            return allocatedframe;
        }

        public static void deallocate(ArrayList<Proces> procList) {

            for (Page page: pageTable) {
                if (page.PB == 1){
                    freeFrame(page.frameNum, procList);
                }
            }
        }

        private static void freeFrame(int frameNum, ArrayList<Proces> procList) {

            //get process with the least frames
            int temp_Aantalframes = 100;
            int temp_procID = -1;
            for (Proces proces : procList) {
                int aantalFrames = 0;
                for (Page page : proces.pageTable) {
                    if (page.PB == 1) {
                        aantalFrames = aantalFrames + 1;
                    }
                }
                if(aantalFrames < temp_Aantalframes){
                    temp_Aantalframes = aantalFrames;
                    temp_procID = proces.pid;
                }
            }

            //load the page with het highest LRU
            int LRU_time = -10;
            int LRU_index = -1;
            for (Page p : procList.get(temp_procID).pageTable) {
                if(p.PB == 0 && p.LRU > LRU_time){
                    LRU_time = p.LRU;
                    LRU_index = procList.get(temp_procID).pageTable.indexOf(p);
                }
            }
            Proces proc = procList.get(temp_procID);
            Page page = proc.pageTable.get(LRU_index);
            page.PB = 1;
            page.frameNum = frameNum;


        }
    }

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