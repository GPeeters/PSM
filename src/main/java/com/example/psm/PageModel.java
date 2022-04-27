package com.example.psm;
import javafx.beans.property.SimpleIntegerProperty;

public class PageModel {
    private SimpleIntegerProperty PB;
    private SimpleIntegerProperty MB;
    private SimpleIntegerProperty LAT;
    private SimpleIntegerProperty frameNumber;

    public PageModel(Integer PB, Integer MB, Integer LAT, Integer frameNumber) {
        this.PB = new SimpleIntegerProperty(PB);
        this.MB = new SimpleIntegerProperty(MB);
        this.LAT = new SimpleIntegerProperty(LAT);
        this.frameNumber = new SimpleIntegerProperty(frameNumber);
    }

    public int getPB() {
        return PB.get();
    }

    public SimpleIntegerProperty PBProperty() {
        return PB;
    }

    public void setPB(int PB) {
        this.PB.set(PB);
    }

    public int getMB() {
        return MB.get();
    }

    public SimpleIntegerProperty MBProperty() {
        return MB;
    }

    public void setMB(int MB) {
        this.MB.set(MB);
    }

    public int getLAT() {
        return LAT.get();
    }

    public SimpleIntegerProperty LATProperty() {
        return LAT;
    }

    public void setLAT(int LAT) {
        this.LAT.set(LAT);
    }

    public int getFrameNumber() {
        return frameNumber.get();
    }

    public SimpleIntegerProperty frameNumberProperty() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNum) {
        this.frameNumber.set(frameNum);
    }
}
