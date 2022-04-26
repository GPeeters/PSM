package com.example.psm;

import java.util.ArrayList;

public class Proces {
    int pid;
    static PageTable pageTable;

    public Proces(int pid){
        this.pid = pid;
        this.pageTable = new PageTable();

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

    public static PageTable getPageTable() {
        return pageTable;
    }

    public static void setPageTable(PageTable pageTable) {
        Proces.pageTable = pageTable;
    }

    public int getframe(ArrayList<Proces> procList) {
        //schrijf hier methode om een frame te alloceren

        //get process with the most frames
        int temp_Aantalframes = -1;
        int temp_procID = -1;
        for (Proces proces : procList) {
            int aantalFrames = 0;
            for (int i=0; i<16; i++) {
                Page page = pageTable.getPage(i);
                if (page.PB == 1) {
                    aantalFrames = aantalFrames + 1;
                }
            }
            if(aantalFrames > temp_Aantalframes){
                temp_Aantalframes = aantalFrames;
                temp_procID = proces.pid;
            }
        }

        //unload the page with the least accessed time
        int LRU_time = 100000000;
        int LRU_index = -1;
        for (Page p : procList.get(temp_procID).pageTable) {
            if(p.PB ==1 && p.LAT < LRU_time){
                LRU_time = p.LAT;
                LRU_index = procList.get(temp_procID).pageTable.indexOf(p);
            }
        }
        Page p = procList.get(temp_procID).pageTable.get(LRU_index);
        p.PB =0;
        int allocatedframe = p.frameNum;
        p.frameNum =-1;

        //return the number of the allocated frame
        return allocatedframe;
    }

    public static void deallocate(ArrayList<Proces> procList) {
        for (Page page: pageTable) {
            if (page.PB == 1){
                freeFrame(page.frameNum, procList);
            }
        }
    }

    private static void freeFrame(int frameNum, ArrayList<Proces> procList) {

        //get process with the least frames
        int temp_Aantalframes = 100;
        int temp_procID = -1;
        for (Proces proces : procList) {
            int aantalFrames = 0;
            for (Page page : proces.pageTable) {
                if (page.PB == 1) {
                    aantalFrames = aantalFrames + 1;
                }
            }
            if(aantalFrames < temp_Aantalframes){
                temp_Aantalframes = aantalFrames;
                temp_procID = proces.pid;
            }
        }

        //load the page with het highest LRU
        int LRU_time = -10;
        int LRU_index = -1;
        for (Page p : procList.get(temp_procID).pageTable) {
            if(p.PB == 0 && p.LAT > LRU_time){
                LRU_time = p.LAT;
                LRU_index = procList.get(temp_procID).pageTable.indexOf(p);
            }
        }
        Proces proc = procList.get(temp_procID);
        Page page = proc.pageTable.get(LRU_index);
        page.PB = 1;
        page.frameNum = frameNum;


    }
}
