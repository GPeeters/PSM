package com.example.psm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.psm.PSMApplication.*;
import static com.example.psm.RAM.frames;

public class PSMController implements Initializable {
    boolean done = false;

    @FXML
    private TableView<PageModel> tbData;

    @FXML
    public TableColumn<PageModel, Integer> PB;

    @FXML
    public TableColumn<PageModel, Integer> MB;

    @FXML
    public TableColumn<PageModel, Integer> LAT;

    @FXML
    public TableColumn<PageModel, Integer> frameNumber;

    @FXML
    private TableView<PageModel> tbData1;

    @FXML
    public TableColumn<PageModel, Integer> PB1;

    @FXML
    public TableColumn<PageModel, Integer> MB1;

    @FXML
    public TableColumn<PageModel, Integer> LAT1;

    @FXML
    public TableColumn<PageModel, Integer> PID;

    @FXML
    private Label l1, l2, timerText, timer, instruct, pid, prevAdd, nextAdd, writeCount, writesFromRAM, physAdd;

    @FXML
    public void setLabelsInit(ArrayList<Instructie> active) {
        l1.setText("File Selected: Instructions_30_3.xml");
        l2.setText(" ");
        timerText.setText("TIMER: ");
        timer.setText("0");
        instruct.setText("Instructie: " + active.get(0).op);
        pid.setText("ID: " + active.get(0).pid);
        prevAdd.setText("PrevADD: " + active.get(0).add);
        nextAdd.setText("PrevADD: " + active.get(0).add);
        writeCount.setText("-");
        writesFromRAM.setText("-");
        physAdd.setText("Reële adres: ");
    }
    @FXML
    private Button buttonSingle, buttonAll;


    @FXML
    public void oneProcess() {
        // TODO acties die gepaard gaan met 1 proces uitvoeren hier implementeren
        if(!done){
            if(index==active.size()-1){
                done = true;
                terminal = "Change file to continue.";
                updateView();
                // throw new RuntimeException("End of file reached, try changing files.");
            }
            terminal = "1 proces werd uitgevoerd";
            executeAction();
            updateView();
            index = index+1;
        } else {
            terminal = "Change file to continue.";
            updateView();
        }
    }
    public void allProcess() {
        // TODO acties die gepaard gaan met alles uitvoeren hier implementeren
        terminal = "Alle processen worden uitgevoerd";
        if(!done){
            while (index < active.size()-1) {
                executeAction();
                updateView();
                index = index+1;
            }
            done = true;
            terminal = "Change file to continue.";
            updateView();
        } else {
            terminal = "Change file to continue.";
            updateView();
            //throw new RuntimeException("End of file reached, try changing files.");
        }
    }

    @FXML
    private Menu menu;
    private MenuItem file1, file2, file3;

    public void actionFile1(){
        done = false;
        setActive(1);
        updateView();
    }
    public void actionFile2(){
        done = false;
        setActive(2);
        updateView();
    }
    public void actionFile3(){
        done = false;
        setActive(3);
        updateView();
    }

    public void updateView(){
        l1.setText("File Selected: "+getFileName()+".xml");
        l2.setText(getTerminal());
        // timerText.setText("TIMER: ");
        timer.setText(""+getTime());
        instruct.setText("Instructie: " + getActive().op);
        pid.setText("ID: " + getActive().pid);
        prevAdd.setText("PrevADD: " + getActive().add);
        nextAdd.setText("NextADD: " + getNextAddress());
        writeCount.setText(""+writeCounter);
        writesFromRAM.setText(""+writeCounter);
        physAdd.setText("Reële adres: " + realAdd(getActive().add, getActive().pid));
        Page[] PT = Plist[getActive().pid].getPT();
        PageModels = FXCollections.observableArrayList(
                new PageModel(PT[0].PB, PT[0].MB, PT[0].LAT, PT[0].frameNumber),
                new PageModel(PT[1].PB, PT[1].MB, PT[1].LAT, PT[1].frameNumber),
                new PageModel(PT[2].PB, PT[2].MB, PT[2].LAT, PT[2].frameNumber),
                new PageModel(PT[3].PB, PT[3].MB, PT[3].LAT, PT[3].frameNumber),
                new PageModel(PT[4].PB, PT[4].MB, PT[4].LAT, PT[4].frameNumber),
                new PageModel(PT[5].PB, PT[5].MB, PT[5].LAT, PT[5].frameNumber),
                new PageModel(PT[6].PB, PT[6].MB, PT[6].LAT, PT[6].frameNumber),
                new PageModel(PT[7].PB, PT[7].MB, PT[7].LAT, PT[7].frameNumber),
                new PageModel(PT[8].PB, PT[8].MB, PT[8].LAT, PT[8].frameNumber),
                new PageModel(PT[9].PB, PT[9].MB, PT[9].LAT, PT[9].frameNumber),
                new PageModel(PT[10].PB, PT[10].MB, PT[10].LAT, PT[10].frameNumber),
                new PageModel(PT[11].PB, PT[11].MB, PT[11].LAT, PT[11].frameNumber),
                new PageModel(PT[12].PB, PT[12].MB, PT[12].LAT, PT[12].frameNumber),
                new PageModel(PT[13].PB, PT[13].MB, PT[13].LAT, PT[13].frameNumber),
                new PageModel(PT[14].PB, PT[14].MB, PT[14].LAT, PT[14].frameNumber),
                new PageModel(PT[15].PB, PT[15].MB, PT[15].LAT, PT[15].frameNumber)
        );
        tbData.setItems(PageModels);

        FrameModels = FXCollections.observableArrayList(
                new PageModel(frames[0].PB, frames[0].MB, frames[0].LAT, frames[0].PID),
                new PageModel(frames[1].PB, frames[1].MB, frames[1].LAT, frames[1].PID),
                new PageModel(frames[2].PB, frames[2].MB, frames[2].LAT, frames[2].PID),
                new PageModel(frames[3].PB, frames[3].MB, frames[3].LAT, frames[3].PID),
                new PageModel(frames[4].PB, frames[4].MB, frames[4].LAT, frames[4].PID),
                new PageModel(frames[5].PB, frames[5].MB, frames[5].LAT, frames[5].PID),
                new PageModel(frames[6].PB, frames[6].MB, frames[6].LAT, frames[6].PID),
                new PageModel(frames[7].PB, frames[7].MB, frames[7].LAT, frames[7].PID),
                new PageModel(frames[8].PB, frames[8].MB, frames[8].LAT, frames[8].PID),
                new PageModel(frames[9].PB, frames[9].MB, frames[9].LAT, frames[9].PID),
                new PageModel(frames[10].PB, frames[10].MB, frames[10].LAT, frames[10].PID),
                new PageModel(frames[11].PB, frames[11].MB, frames[11].LAT, frames[11].PID)
        );

        tbData1.setItems(FrameModels);
    }

    private int realAdd(int add, int pid) {
        int realAdd = 0;

        return realAdd;
    }

    // add your data here from any source
    Page[] PT = Plist[index].getPT();
    private ObservableList<PageModel> PageModels = FXCollections.observableArrayList(
            new PageModel(PT[0].PB, PT[0].MB, PT[0].LAT, PT[0].frameNumber),
            new PageModel(PT[1].PB, PT[1].MB, PT[1].LAT, PT[1].frameNumber),
            new PageModel(PT[2].PB, PT[2].MB, PT[2].LAT, PT[2].frameNumber),
            new PageModel(PT[3].PB, PT[3].MB, PT[3].LAT, PT[3].frameNumber),
            new PageModel(PT[4].PB, PT[4].MB, PT[4].LAT, PT[4].frameNumber),
            new PageModel(PT[5].PB, PT[5].MB, PT[5].LAT, PT[5].frameNumber),
            new PageModel(PT[6].PB, PT[6].MB, PT[6].LAT, PT[6].frameNumber),
            new PageModel(PT[7].PB, PT[7].MB, PT[7].LAT, PT[7].frameNumber),
            new PageModel(PT[8].PB, PT[8].MB, PT[8].LAT, PT[8].frameNumber),
            new PageModel(PT[9].PB, PT[9].MB, PT[9].LAT, PT[9].frameNumber),
            new PageModel(PT[10].PB, PT[10].MB, PT[10].LAT, PT[10].frameNumber),
            new PageModel(PT[11].PB, PT[11].MB, PT[11].LAT, PT[11].frameNumber),
            new PageModel(PT[12].PB, PT[12].MB, PT[12].LAT, PT[12].frameNumber),
            new PageModel(PT[13].PB, PT[13].MB, PT[13].LAT, PT[13].frameNumber),
            new PageModel(PT[14].PB, PT[14].MB, PT[14].LAT, PT[14].frameNumber),
            new PageModel(PT[15].PB, PT[15].MB, PT[15].LAT, PT[15].frameNumber)
    );

    private ObservableList<PageModel> FrameModels = FXCollections.observableArrayList(
            new PageModel(frames[0].PB, frames[0].MB, frames[0].LAT, frames[0].PID),
            new PageModel(frames[1].PB, frames[1].MB, frames[1].LAT, frames[1].PID),
            new PageModel(frames[2].PB, frames[2].MB, frames[2].LAT, frames[2].PID),
            new PageModel(frames[3].PB, frames[3].MB, frames[3].LAT, frames[3].PID),
            new PageModel(frames[4].PB, frames[4].MB, frames[4].LAT, frames[4].PID),
            new PageModel(frames[5].PB, frames[5].MB, frames[5].LAT, frames[5].PID),
            new PageModel(frames[6].PB, frames[6].MB, frames[6].LAT, frames[6].PID),
            new PageModel(frames[7].PB, frames[7].MB, frames[7].LAT, frames[7].PID),
            new PageModel(frames[8].PB, frames[8].MB, frames[8].LAT, frames[8].PID),
            new PageModel(frames[9].PB, frames[9].MB, frames[9].LAT, frames[9].PID),
            new PageModel(frames[10].PB, frames[10].MB, frames[10].LAT, frames[10].PID),
            new PageModel(frames[11].PB, frames[11].MB, frames[11].LAT, frames[11].PID)
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        l1.setText("File Selected: Instructions_30_3.xml");
        l2.setText("Click a button to start");
        timerText.setText("TIMER: ");
        timer.setText("0");
        instruct.setText("Instructie: " + "Start");
        pid.setText("ID: " + 0);
        prevAdd.setText("PrevADD: " + 0);
        nextAdd.setText("NextADD: " + 0);
        physAdd.setText("Reële adres: " + 0);

        PB.setCellValueFactory(new PropertyValueFactory<>("PB"));
        MB.setCellValueFactory(new PropertyValueFactory<>("MB"));
        LAT.setCellValueFactory(new PropertyValueFactory<>("LAT"));
        frameNumber.setCellValueFactory(new PropertyValueFactory<>("frameNumber"));

        tbData.setItems(PageModels);

        PB1.setCellValueFactory(new PropertyValueFactory<>("PB"));
        MB1.setCellValueFactory(new PropertyValueFactory<>("MB"));
        LAT1.setCellValueFactory(new PropertyValueFactory<>("LAT"));
        PID.setCellValueFactory(new PropertyValueFactory<>("frameNumber"));

        tbData1.setItems(FrameModels);
    }

}