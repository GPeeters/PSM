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
    }

    // add your data here from any source
    Page[] PT = Plist[i].getPT();
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
        tbData.setItems(PageModels);
    }

}