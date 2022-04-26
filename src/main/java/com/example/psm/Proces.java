package com.example.psm;

import java.util.ArrayList;

public class Proces {
    int pid;
    static Page[] PT;
    static ArrayList<Page> framesInRam;

    public Proces(int pid){
        Page[] PageT = new Page[16];
        for(int i=0; i<16; i++){
            PageT[i] = new Page(0, 0, 0, i, pid);
        }

        this.pid = pid;
        PT = PageT;
        framesInRam = new ArrayList<>();

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

    public void addPageToRAM(int pnr){
        PT[pnr].setPB(1);
        framesInRam.add(PT[pnr]);
    }
    // finds the first page that is not in the RAM yet
    public int addPageToRAM(){
        int pnr = 0;
        ArrayList<Integer> pagesInRam = new ArrayList<>();
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
        framesInRam.add(PT[pnr]);
        return pnr;
    }

    public void removePageFromRAM(int pnr){
        framesInRam.remove(pnr);
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

}
