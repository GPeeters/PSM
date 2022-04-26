package com.example.psm;

import java.util.ArrayList;

public class Proces {
    int pid;
    static Page[] PT;
    static ArrayList<Page> framesInRam;

    public Proces(int pid){
        Page[] PageT = new Page[16];
        for(int i=0; i<16; i++){
            PT[i] = new Page(0, 0, 0, i, pid);
        }

        this.pid = pid;
        this.PT = PageT;
        this.framesInRam = new ArrayList<>();

//        try{
//            for (int i = 0; i < 12/(procList.size()+1); i++) {
//                pageTable.add(new Page(1,0, -1, getframe(procList)));
//            }
//            for (int i = 0; i < 16-(12/(procList.size()+1)); i++) {
//                pageTable.add(new Page(0, 0, -1, -1));
//            }
//        }
//        catch (Exception e) {
//            for (int i = 0; i < 12; i++) {
//                pageTable.add(new Page(1,0, -1, i));
//            }
//            for (int i = 0; i < 4; i++) {
//                pageTable.add(new Page(0, -1, -1, -1));
//            }
//        }

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

    public int addressToPageNr(int address){
        int PageNr = (int) Math.floor(address/4096);
        return PageNr;
    }

    public static void deallocate() {
        framesInRam = new ArrayList<>();
        for (Page page: PT) {
            if (page.PB == 1){
                page.setPB(0);
            }
        }
    }

}
