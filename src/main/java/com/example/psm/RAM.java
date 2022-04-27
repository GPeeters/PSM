package com.example.psm;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.psm.PSMApplication.*;

public class RAM {
    static int aantalProc;
    static Page[] frames = new Page[12];
    // totale grootte: 49152
    // grootte van 1 frame: 4096

    public RAM() {
        aantalProc = 0;
        frames = new Page[12];
        for(int i=0;i<12;i++){
            frames[i] = new Page();
        }
    }

    public static void setAantalProc(int aantalProc) {
        RAM.aantalProc = aantalProc;
    }

    public ArrayList<Integer> getPIDs(){
        ArrayList<Integer> PIDs = new ArrayList<>();
        for(int i=0; i<12; i++){
            PIDs.add(i, frames[i].getPID());
        }
        return PIDs;
    }

    public boolean addToRAM(Proces p) {
        // Check if still room in RAM
        int PID = p.getPid();
        int PID2;
        int PID3;
        int PID4;

        ArrayList<Integer> procInRam = getActiveProcs();

        if (aantalProc < 4){
            switch (aantalProc){
                case 0:
                    setAantalProc(1);
                    for(int i=0; i<12; i++){
                        Plist[PID].setPageTime(i);
                        frames[i] = Plist[PID].getPage(i);
                        p.addPageToRAM(i,i);
                    }
                    break;

                case 1:
                    setAantalProc(2);
                    PID2 = procInRam.get(0);
                    for(int i=0; i<6; i++){
                        int released = getframe(Plist[PID2]);
                        Plist[PID].setPageTime(i);
                        frames[released] = Plist[PID].getPage(i);
                        p.addPageToRAM(i, released);
                    }
                    break;

                case 2:
                    setAantalProc(3);
                    PID2 = procInRam.get(0);
                    PID3 = procInRam.get(1);
                    for(int i=0; i<2; i++){
                        int r1 = getframe(Plist[PID2]);
                        int r2 = getframe(Plist[PID3]);
                        Plist[PID].setPageTime(i);
                        Plist[PID].setPageTime(i+2);
                        frames[r1] = Plist[PID].getPage(i);
                        frames[r2] = Plist[PID].getPage(i+2);
                        p.addPageToRAM(i, r1);
                        p.addPageToRAM(i+2, r2);
                    }
                    break;

                case 3:
                    setAantalProc(4);
                    PID2 = procInRam.get(0);
                    PID3 = procInRam.get(1);
                    PID4 = procInRam.get(2);

                    int r1 = getframe(Plist[PID2]);
                    int r2 = getframe(Plist[PID3]);
                    int r3 = getframe(Plist[PID4]);

                    frames[r1] = Plist[PID].getPage(0);
                    frames[r2] = Plist[PID].getPage(1);
                    frames[r3] = Plist[PID].getPage(2);

                    p.addPageToRAM(0, r1);
                    p.addPageToRAM(1, r2);
                    p.addPageToRAM(2, r3);

                    break;

                default:
                    throw new RuntimeException("Amount of processes in RAM is negative. This should not be possible.");
            }
            return true;
        } else {
            return false;
        }
    }

    public void removeFromRAM(Proces p) {
        int p2 = -1;
        int p3 = -1;
        int p4 = -1;

        ArrayList<Integer> PIDs = getPIDs();
        int index = PIDs.indexOf(p.getPid());

        switch (aantalProc){
            case 1:
                setAantalProc(0);
                for(int i=0;i<12;i++){
                    getframe(p);
                }
                break;
                // RAM moet niet toegewezen worden tot er een nieuw proces in komt
            case 2:
                setAantalProc(1);
                for(int i=0; i<12; i++){
                    if(frames[i].getPID() != p.getPid()){
                        p2 = frames[i].PID;
                        break;
                    }
                }

                if(p2 == -1){
                    throw new RuntimeException("Tried to remove a process from RAM that wasn't there");
                }

                for(int i=0;i<6;i++){
                    int fnr =  getframe(p);
                    int pnr = Plist[p2].findPageNotInRAM();
                    addPageToRAM(p2, pnr, fnr);
                }

                break;

            case 3:
                setAantalProc(2);

                for(int i=0; i<12; i++){
                    if(frames[i].getPID() != p.getPid()){
                        p2 = frames[i].PID;
                        break;
                    }
                }

                for(int i=0; i<12; i++){
                    if(frames[i].getPID() != p.getPid() && frames[i].getPID() != p2){
                        p3 = frames[i].PID;
                        break;
                    }
                }
                if(p2 == -1 || p3 == -1){
                    throw new RuntimeException("Tried to remove a process from RAM that wasn't there");
                }

                for(int i=0;i<2;i++){
                    int fnr =  getframe(p);
                    int pnr = Plist[p2].findPageNotInRAM();
                    addPageToRAM(p2, pnr, fnr);
                }

                for(int i=0;i<2;i++){
                    int fnr =  getframe(p);
                    int pnr = Plist[p3].findPageNotInRAM();
                    addPageToRAM(p3, pnr, fnr);
                }
                break;

            case 4:
                setAantalProc(3);

                for(int i=0; i<12; i++){
                    if(frames[i].getPID() != p.getPid()){
                        p2 = frames[i].PID;
                        break;
                    }
                }

                for(int i=0; i<12; i++){
                    if(frames[i].getPID() != p.getPid() && frames[i].getPID() != p2){
                        p3 = frames[i].PID;
                        break;
                    }
                }

                for(int i=0; i<12; i++){
                    if(frames[i].getPID() != p.getPid() && frames[i].getPID() != p2 && frames[i].getPID() != p3){
                        p4 = frames[i].PID;
                        break;
                    }
                }
                if(p2 == -1 || p3 == -1 || p4 ==-1){
                    throw new RuntimeException("Tried to remove a process from RAM that wasn't there");
                }

                addPageToRAM(p3, Plist[p2].findPageNotInRAM(), getframe(p));
                addPageToRAM(p3, Plist[p3].findPageNotInRAM(), getframe(p));
                addPageToRAM(p4, Plist[p4].findPageNotInRAM(), getframe(p));

                break;

            default:
                throw new RuntimeException("Amount of processes in RAM is negative. This should not be possible.");
        }
    }


    public static void switchPageToRAM(int PNR, int pid) {
        int frameNr = getframe(Plist[pid]);
        // mogelijks werkt dit niet correct
        Plist[pid].addPageToRAM(PNR, frameNr);
        frames[frameNr] = Plist[pid].getPage(PNR);
        writeCounter += 1;
    }

    public int addressToPID(int address){
        int frameNr = (int) Math.floor(address/4096);
        int PID = frames[frameNr].getPID();

        return PID;
    }

    public static int getframe(Proces p) {
        //unload the page with the lowest accessed time
        int LRU_time = 100000000;
        int LRU_index = -1;
        for (Page pg : p.getFramesInRam()) {
            if(pg.LAT < LRU_time){
                int temp = getFrameIndex(pg);
                if(temp != -1){
                    LRU_time = pg.LAT;
                    LRU_index = getFrameIndex(pg);
                }
            }
        }

        if(LRU_index == -1){
            throw new RuntimeException("No frame seems to be available for this process");
        }

        if(frames[LRU_index].MB == 1){
            writeFromRam(frames[LRU_index].PID, frames[LRU_index].pageNr, frames[LRU_index].frameNumber);
        }
        frames[LRU_index].PB = 0;
        p.removePageFromRAM(frames[LRU_index].pageNr);
        // frames[LRU_index].frameNumber = -1;

        //return the number of the allocated frame
        return LRU_index;
    }

    public static int getFrameIndex(Page p){
        int getIndex = Arrays.asList(frames).indexOf(p);
        return getIndex;
    }

    public ArrayList<Integer> getActiveProcs(){
        ArrayList<Integer> procs = new ArrayList<>();
        if(aantalProc != 0){
            for(int i=0; i<12; i++){
                if(!procs.contains(frames[i].getPID())){
                    procs.add(frames[i].getPID());
                }
            }
        }
        return procs;
    }

    public static void writeOverRam(int pid, int pnr, int fnr){
        if(frames[fnr].MB == 1){
            writeFromRam(pid, pnr, fnr);
        }
        Plist[pid].addPageToRAM(pnr, fnr);
        frames[fnr] = Plist[pid].getPage(pnr);
        frames[fnr].MB = 1;
        writeCounter += 1;
    }

    public static void writeToRam(int pid, int pnr, int fnr){
        Plist[pid].addPageToRAM(pnr, fnr);
        frames[fnr] = Plist[pid].getPage(pnr);
        frames[fnr].MB = 1;
        writeCounter += 1;
    }

    public static void writeFromRam(int pid, int pnr, int fnr){
        frames[fnr].LAT = time;
        frames[fnr].MB = 0;
        Plist[pid].setPage(pnr, frames[fnr]);
        writeCounter += 1;
    }

    public void addPageToRAM(int pid, int pnr, int fnr){
        Plist[pid].addPageToRAM(pnr, fnr);
        frames[fnr] = Plist[pid].getPage(pnr);
        writeCounter += 1;
    }

    public static void setFrameTime(int fnr){
        frames[fnr].LAT = time;
    }
}
