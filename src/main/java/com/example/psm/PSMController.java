package com.example.psm;

import com.example.psm.PageStrategySimulator.Instructie;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.psm.PSMApplication.*;

public class PSMController implements Initializable {

    @FXML
    private Label l1, l2, timerText, timer, instruct, pid, prevAdd, nextAdd;

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
    }
}