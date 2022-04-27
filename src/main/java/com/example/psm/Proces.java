package com.example.psm;

import java.util.ArrayList;

import static com.example.psm.PSMApplication.writeCounter;
import static com.example.psm.RAM.getframe;

public class Proces {
    int pid;
    private Page[] PT;

    public Proces(int pid){
        PT = new Page[16];
        for(int i=0; i<16; i++){
            PT[i] = new Page(0, 0, 0, i, pid);
        }

        this.pid = pid;

    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getVirtAdd() {
        return pid*4096;
    }

    public Page[] getPT() {
        return PT;
    }

    public void setPT(Page[] PT) {
        this.PT = PT;
    }

    // function may not be used on its own, frameNr still has to be appointed
    public void addPageToRAM(int pnr, int fnr){
        PT[pnr].setPB(1);
        PT[pnr].setFrameNumber(fnr);
    }
    // finds the first page that is not in the RAM yet
    public int addPageToRAM(){
        int pnr = 0;
        ArrayList<Integer> pagesInRam = new ArrayList<>();
        ArrayList<Page> framesInRam = getFramesInRam();
        for(Page page: framesInRam){
            pagesInRam.add(page.getPageNr());
        }
        for(int i=0; i<16; i++){
            if(!pagesInRam.contains(i)){
                pnr = i;
                break;
            }
        }

        PT[pnr].setPB(1);
        return pnr;
    }

    public void removePageFromRAM(int pnr){
        setPagePBZero(pnr);
    }

    public Page getPage(int nr) {
        if(0 <= nr && nr < 16){
            return PT[nr];
        } else {
            throw new RuntimeException("Page number out of bounds [0, 15]");
        }
    }

    public void setPage(int nr, Page pg) {
        if(0 <= nr && nr < 16){
            PT[nr] = pg;
        } else {
            throw new RuntimeException("Page number out of bounds [0, 15]");
        }
    }

    public void setPagePBZero(int nr) {
        if(0 <= nr && nr < 16){
            PT[nr].setPB(0);
        } else {
            throw new RuntimeException("Page number out of bounds [0, 15]");
        }
    }

    public void setFrameNumber(int pageNr, int frameNumber) {
        PT[pageNr].setFrameNumber(frameNumber);
    }

    public ArrayList<Page> getFramesInRam(){
        ArrayList<Page> FIR = new ArrayList<>();
        for(int i=0;i<16;i++){
            if(PT[i].PB == 1){
                FIR.add(PT[i]);
            }
        }
        return FIR;
    }

    public void setMB1(int pnr) {
        if(PT[pnr].PB == 1){
            if(PT[pnr].MB == 1){
                RAM.writeToRam(pid, pnr, PT[pnr].frameNumber);
            } else {
                RAM.writeToRam(pid, pnr, PT[pnr].frameNumber);
                PT[pnr].MB = 1;
            }
        } else {
            PT[pnr].MB = 1;
            addPageToRAM(pnr, getframe(this));
        }

    }
    public void setMB0(int pnr) {
        PT[pnr].MB = 0;
    }
}
