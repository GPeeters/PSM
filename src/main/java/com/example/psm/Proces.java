package com.example.psm;

import java.util.ArrayList;

import static com.example.psm.PSMApplication.time;
import static com.example.psm.PSMApplication.writeCounter;
import static com.example.psm.RAM.getframe;
import static com.example.psm.RAM.writeFromRam;

public class Proces {
    int pid;
    private Page[] PT;

    public Proces(int pid){
        PT = new Page[16];
        for(int i=0; i<16; i++){
            PT[i] = new Page(0, 0, 99999, i, pid);
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

    public void addPageToRAM(int pnr, int fnr){
        PT[pnr].PB = 1;
        PT[pnr].frameNumber = fnr;
    }

    // finds the first page that is not in the RAM yet
    public int findPageNotInRAM(){
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
        return pnr;
    }

    public void removePageFromRAM(int pnr){
        if(PT[pnr].MB == 1){
            writeFromRam(pid, pnr, PT[pnr].frameNumber);
        }
        PT[pnr].PB = 0;
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
            // pg.MB = 0;
            PT[nr] = pg;
            PT[nr].frameNumber = -1;
            writeCounter += 1;
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

    public void write(int pnr) {
        PT[pnr].LAT = time;
        if(PT[pnr].PB == 1){
            RAM.writeOverRam(pid, pnr, PT[pnr].frameNumber);
        } else {
            PT[pnr].PB = 1;
            PT[pnr].MB = 1;
            RAM.writeToRam(pid, pnr, getframe(this));
        }

    }
    public void setMB(int pnr, int b) {
        PT[pnr].MB = b;
    }

    public void setPagePB(int pnr, int pb) {
        PT[pnr].PB = pb;
    }

    public void setPageFrameNumber(int pnr, int fn) {
        PT[pnr].frameNumber = fn;
    }

    public void setPageTime(int pnr) {
        PT[pnr].LAT = time;
    }
}
