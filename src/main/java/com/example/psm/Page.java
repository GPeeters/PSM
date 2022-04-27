package com.example.psm;

public class Page{
    int PB;
    int MB;
    int LAT;
    int frameNumber;
    int pageNr;
    int PID;

    // Page bestaat uit: a) een present bit, (b) een modify bit, (c) de last access time, en (d) het corresponderende
    //framenummer. Het framenummer ligt in het interval [0,11].
    // groote: 4096

    public Page() {
        this.PB = 0;
        this.MB = 0;
        this.LAT = 99999;
        this.frameNumber = -1;
        this.pageNr = -1;
        this.PID = -1;
    }
    public Page(int PB, int MB, int LAT, int pageNr, int pid) {
        this.PB = PB;
        this.MB = MB;
        this.LAT = LAT;
        this.frameNumber = -1;
        this.pageNr = pageNr;
        this.PID = pid;
    }

    public void setPB(int PB) {
        this.PB = PB;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public int getPageNr() {
        return pageNr;
    }

    public int getPID() {
        return PID;
    }

}