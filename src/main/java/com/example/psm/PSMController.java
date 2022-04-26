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

public class PSMController implements Initializable {
    @FXML
    private TableView table = new TableView();

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
    private Label l1, l2, timerText, timer, instruct, pid, prevAdd, nextAdd;

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
    }
    @FXML
    private Button buttonSingle, buttonAll;


    @FXML
    public void oneProcess() {
        // TODO acties die gepaard gaan met 1 proces uitvoeren hier implementeren
        terminal = "1 proces werd uitgevoerd";
        executeAction();
        updateView();
        if(i==active.size()-1) i = 0;
        else i = i+1;
    }
    public void allProcess() {
        // TODO acties die gepaard gaan met alles uitvoeren hier implementeren
        terminal = "Alle processen worden uitgevoerd";
        while (i < active.size()-1) {
            executeAction();
            updateView();
            i = i+1;
        }
        terminal = "Done";
        updateView();
    }

    @FXML
    private Menu menu;
    private MenuItem file1, file2, file3;

    public void actionFile1(){
        setActive(1);
    }

    public void actionFile2(){
        setActive(2);
    }

    public void actionFile3(){
        setActive(3);
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
    }

    // add your data here from any source
    private ObservableList<PageModel> PageModels = FXCollections.observableArrayList(
            new PageModel(Plist[0].PT[0].PB,Plist[0].PT[0].MB, Plist[0].PT[0].LAT, Plist[0].PT[0].frameNum),
            new PageModel(Plist[0].PT[1].PB,Plist[0].PT[1].MB, Plist[0].PT[1].LAT, Plist[0].PT[1].frameNum)
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

        //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
        PB.setCellValueFactory(new PropertyValueFactory<>("PB"));
        MB.setCellValueFactory(new PropertyValueFactory<>("MB"));
        LAT.setCellValueFactory(new PropertyValueFactory<>("LAT"));
        frameNumber.setCellValueFactory(new PropertyValueFactory<>("frameNumber"));
        //add your data to the table here.
        // tbData.setItems(PageModels);
    }

}