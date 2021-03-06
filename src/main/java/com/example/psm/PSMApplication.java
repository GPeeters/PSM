package com.example.psm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.psm.RAM.switchPageToRAM;
import static com.example.psm.ReadXML.ReadXML;


public class PSMApplication extends Application {
    private static final String FILENAME1 = "Instructions_30_3.xml";
    private static final String FILENAME2 = "Instructions_20000_4.xml";
    private static final String FILENAME3 = "Instructions_20000_20.xml";

    static ArrayList<Instructie> active;
    static ArrayList<Instructie> pro_30_3;
    static ArrayList<Instructie> pro_20000_4;
    static ArrayList<Instructie> pro_20000_20;

    public static Proces[] Plist;
    public static RAM Ram = new RAM();

    static int index;
    static int time;
    static int writeCounter = 0;
    static int toRamCounter = 0;
    static int fromRamCounter = 0;

    static String activeName = "Instructions_30_3";
    static String terminal;


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PSMApplication.class.getResource("appView.fxml"));
        PSMController controller = new PSMController();
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("Page Strategy Simulator GUI");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // First read in all xml files;
        pro_30_3 = ReadXML(FILENAME1);
        pro_20000_4 = ReadXML(FILENAME2);
        pro_20000_20 = ReadXML(FILENAME3);

        active = pro_30_3;
        activeName = "Instructions_30_3";

        index = 0;
        time = 0;

        Plist = new Proces[20];
        for (int j = 0; j < 20; j++) {
            Plist[j] = new Proces(j);
        }

        terminal = "Click a button to start";

        launch();
    }

    public static void executeAction(){
        time = time + 1;
        checkOp(active.get(index));
    }

    public static void checkOp(Instructie p){
        switch (p.op) {
            case "Start":
                startProcess(p.pid);
                break;
            case "Terminate":
                terminateProcess(p.pid);
                break;
            case "Read":
                readProcess(p.add, p.pid);
                break;
            case "Write":
                writeProcess(p.add, p.pid);
                break;
            default:
                System.out.println("ERROR op code unknown");
                break;
        }
    }

    public static void startProcess(int pid){
        //In de constructor van de klasse Process staat de ingewikkelde code.
        Ram.addToRAM(Plist[pid]);
    }

    public static void terminateProcess(int pid){
        if(RAM.getActiveProcs().contains(pid)){
            Ram.removeFromRAM(Plist[pid]);
        } else {
            throw new RuntimeException("Terminating a process that isn't in RAM?");
        }
    }

    public static void readProcess(int add, int pid){
        int PNR = addressToPageNr(add);
        Plist[pid].setPageTime(PNR);
        ArrayList<Integer> pagesInRam = new ArrayList<>();
        ArrayList<Page> framesInRam = Plist[pid].getFramesInRam();
        for(Page page: framesInRam){
            pagesInRam.add(page.getPageNr());
        }

        if(pagesInRam.contains(PNR)){
            // aangezien het adres zich reeds in RAM bevindt moeten we enkel te LAT aanpassen
            RAM.setFrameTime(Plist[pid].getPage(PNR).frameNumber);
        } else {
            switchPageToRAM(PNR, pid);
        }

    }

    public static void writeProcess(int add, int pid){
        int PNR = addressToPageNr(add);
        Plist[pid].write(PNR);
    }


    public static void setActive(int num){
        switch(num){
            case 1:
                active = pro_30_3;
                activeName = "Instructions_30_3";
                reset();
                break;

            case 2:
                active = pro_20000_4;
                activeName = "Instructions_20000_4";
                reset();
                break;

            case 3:
                active = pro_20000_20;
                activeName = "Instructions_20000_20";
                reset();
                break;

            default:
                break;
        }
    }

    public static Instructie getActive(){
        if(index == active.size()){
            return active.get(index-1);
        }
        return active.get(index);
    }

    public static int getTime(){
        return time;
    }
    public static String getFileName(){
        return activeName;
    }
    public static String getTerminal(){
        return terminal;
    }
    public static String getNextAddress() {
        if (index < active.size() - 1) {
            return "" + active.get(index + 1).add;
        } else {
            return "END";
        }
    }

    public static int addressToPageNr(int address){
        return (int) Math.floor(address/4096);
    }

    public static void reset(){
        index = 0;
        time = 0;
        writeCounter = 0;
        fromRamCounter = 0;
        toRamCounter = 0;

        Ram = new RAM();
        for(int j=0;j<12;j++){
            RAM.frames[j] = new Page(0, 0, 0, 0, -1);
        }
        Plist = new Proces[20];
        for(int j=0;j<20;j++){
            Plist[j] = new Proces(j);
        }
    }
}

