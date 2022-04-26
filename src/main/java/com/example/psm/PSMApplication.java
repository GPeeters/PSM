package com.example.psm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

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
    static RAM Ram = new RAM();
    // static HardDrive hd = new HardDrive();

    static int i;
    static int time;

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

        i = 0;
        time = 0;

        Plist = new Proces[20];
        for (int j = 0; j < 20; j++) {
            Plist[20] = new Proces(j);
        }

        terminal = "Press a button to begin";

        launch();
    }

    public static void executeAction(){
        time = time + 1;
        checkOp(active.get(i));
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
                writeProcess();
                break;
            default:
                System.out.println("ERROR op code unknown");
                break;
        }
    }

    public static void startProcess(int pid){
        //In de constructor van de klasse Process staat de ingewikkelde code.
        Ram.addToRAM(new Page(pid));
    }

    public static void terminateProcess(int pid){
        if(Ram.getAantalProc() == 1){
            // TODO clear de hele ram

        } else {
            //Het juiste proces zoeken
            int temp = 0;
            for (Proces proces : procList) {
                if (proces.pid == pid) {
                    break;
                }
                temp++;
            }

            //Het geheugen van het proces vrijgeven
            (procList.get(temp)).deallocate(procList);

            //Het proces verwijderen
            procList.remove(temp);
        }

        hd.clearProc(pid);
    }

    public static void readProcess(int add, int pid){
        hd.setPage(add, pid);
        switchPageToRAM(add, pid);
    }

    private static void switchPageToRAM(int add, int pid) {
        int temp = 0;
        for (Proces proces : procList) {
            if (proces.pid == pid) {
                break;
            }
            temp++;
        }

        int pageNum = add/4096;

    }

    public static void writeProcess(){
        //TODO write implementeren
    }


    public static void setActive(int num){
        switch(num){
            case 1:
                i = 0;
                active = pro_30_3;

            case 2:
                i = 0;
                active = pro_20000_4;

            case 3:
                i = 0;
                active = pro_20000_20;

            default:
                break;
        }
    }

    public static Instructie getActive(){
        return active.get(i);
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
        if (i < active.size() - 1) {
            return "" + active.get(i + 1).add;
        } else {
            return "END";
        }
    }

}

