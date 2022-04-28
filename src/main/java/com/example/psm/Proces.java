package com.example.psm;

import java.util.ArrayList;

import static com.example.psm.PSMApplication.*;
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

    public Page[] getPT() {
        return PT;
    }

    public void addPageToRAM(int pnr, int fnr){
        PT[pnr].PB = 1;
        PT[pnr].frameNumber = fnr;
        PT[pnr].LAT = time;
        writeCounter += 1;
        toRamCounter += 1;
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
        PT[pnr].frameNumber = -1;
        PT[pnr].LAT = time;
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
            PSMApplication.toRamCounter += 1;
        } else {
            throw new RuntimeException("Page number out of bounds [0, 15]");
        }
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

    public void setPageTime(int pnr) {
        PT[pnr].LAT = time;
    }
}
