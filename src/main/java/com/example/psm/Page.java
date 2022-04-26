package com.example.psm;

public class Page{
    int PB;
    int MB;
    int LAT;
    int frameNum;
    int pageNr;

    // Page bestaat uit: a) een present bit, (b) een modify bit, (c) de last access time, en (d) het corresponderende
    //framenummer. Het framenummer ligt in het interval [0,11].
    // groote: 4096

    public Page() {
        this.PB = 0;
        this.MB = 0;
        this.LAT = 0;
        this.frameNum = -1;
        this.pageNr = -1;
    }
    public Page(int PB, int MB, int LAT, int pageNr) {
        this.PB = PB;
        this.MB = MB;
        this.LAT = LAT;
        this.frameNum = -1;
        this.pageNr = pageNr;
    }

    public int getPB() {
        return PB;
    }

    public void setPB(int PB) {
        this.PB = PB;
    }

    public int getMB() {
        return MB;
    }

    public void setMB(int MB) {
        this.MB = MB;
    }

    public int getLAT() {
        return LAT;
    }

    public void setLAT(int LAT) {
        this.LAT = LAT;
    }

    public int getFrameNum() {
        return frameNum;
    }

    public void setFrameNum(int frameNum) {
        this.frameNum = frameNum;
    }

    public int getPageNr() {
        return pageNr;
    }

    public void setPageNr(int pageNr) {
        this.pageNr = pageNr;
    }
}