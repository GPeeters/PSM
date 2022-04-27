package com.example.psm;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.psm.PSMApplication.Plist;

public class RAM {
    static int aantalProc;
    static Page[] frames;
    // totale grootte: 49152
    // grootte van 1 frame: 4096

    public RAM() {
        aantalProc = 0;
        frames = new Page[12];
    }

    public static int getAantalProc() {
        return aantalProc;
    }

    public static void setAantalProc(int aantalProc) {
        RAM.aantalProc = aantalProc;
    }

    public static Page[] getFrames() {
        return frames;
    }

    public static void setFrames(Page[] frames) {
        RAM.frames = frames;
    }

    public ArrayList<Integer> getPIDs(){
        ArrayList<Integer> PIDs = new ArrayList<>(12);
        for(int i=0; i<12; i++){
            PIDs.set(i, frames[i].getPID());
        }
        return PIDs;
    }

    public boolean addToRAM(Proces p) {
        // Check if still room in RAM
        Proces p2;
        Proces p3;
        Proces p4;

        ArrayList<Integer> procInRam = getActiveProcs();

        if (aantalProc < 4){
            switch (aantalProc){
                case 0:
                    setAantalProc(1);
                    for(int i=0; i<12; i++){
                        frames[i] = p.getPT()[i];
                        p.addPageToRAM(i,i);
                    }
                    break;

                case 1:
                    setAantalProc(2);
                    p2 = Plist[procInRam.get(0)];
                    for(int i=0; i<6; i++){
                        int released = getframe(p2);
                        frames[released] = p.getPT()[i];
                        p.addPageToRAM(i, released);
                    }
                    break;

                case 2:
                    setAantalProc(3);
                    p2 = Plist[procInRam.get(0)];
                    p3 = Plist[procInRam.get(1)];
                    for(int i=0; i<2; i++){
                        int r1 = getframe(p2);
                        int r2 = getframe(p3);
                        frames[r1] = p.getPT()[i];
                        frames[r2] = p.getPT()[i+2];
                        p.addPageToRAM(i, r1);
                        p.addPageToRAM(i+2, r2);
                    }
                    break;

                case 3:
                    setAantalProc(4);
                    p2 = Plist[procInRam.get(0)];
                    p3 = Plist[procInRam.get(1)];
                    p4 = Plist[procInRam.get(2)];

                    int r1 = getframe(p2);
                    int r2 = getframe(p3);
                    int r3 = getframe(p4);

                    frames[r1] = p.getPT()[0];
                    frames[r2] = p.getPT()[1];
                    frames[r3] = p.getPT()[2];
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
        Proces p2 = null;
        Proces p3 = null;
        Proces p4 = null;

        int P2ind;
        int P3ind;

        ArrayList<Integer> PIDs = getPIDs();
        int index = PIDs.indexOf(p.getPid());

        // deze if is in principe niet nodig
        if (aantalProc > 0){
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
                            p2 = Plist[i];
                            break;
                        }
                    }

                    for(int i=0;i<6;i++){
                        int pnr =  getframe(p);
                        frames[pnr] = p2.getPT()[p2.addPageToRAM()];
                    }

                    break;

                case 3:
                    setAantalProc(2);
                    P2ind = 0;

                    for(int i=0; i<12; i++){
                        if(frames[i].getPID() != p.getPid()){
                            p2 = Plist[i];
                            P2ind = i;
                            break;
                        }
                    }

                    for(int i=0; i<12; i++){
                        if(frames[i].getPID() != p.getPid() && i != P2ind){
                            p3 = Plist[i];
                            break;
                        }
                    }

                    for(int i=0;i<2;i++){
                        int pnr =  getframe(p);
                        frames[pnr] = p2.getPT()[p2.addPageToRAM()];
                    }

                    for(int i=0;i<2;i++){
                        int pnr =  getframe(p);
                        frames[pnr] = p3.getPT()[p3.addPageToRAM()];
                    }
                    break;

                case 4:
                    setAantalProc(3);

                    P2ind = 0;
                    P3ind = 0;

                    for(int i=0; i<12; i++){
                        if(frames[i].getPID() != p.getPid()){
                            p2 = Plist[i];
                            P2ind = i;
                            break;
                        }
                    }

                    for(int i=0; i<12; i++){
                        if(frames[i].getPID() != p.getPid() && i != P2ind){
                            p3 = Plist[i];
                            P3ind = i;
                            break;
                        }
                    }

                    for(int i=0; i<12; i++){
                        if(frames[i].getPID() != p.getPid() && i != P2ind && i != P3ind){
                            p4 = Plist[i];
                            break;
                        }
                    }

                    frames[getframe(p)] = p2.getPT()[p2.addPageToRAM()];
                    frames[getframe(p)] = p3.getPT()[p3.addPageToRAM()];
                    frames[getframe(p)] = p4.getPT()[p4.addPageToRAM()];

                    break;

                default:
                    throw new RuntimeException("Amount of processes in RAM is negative. This should not be possible.");
            }
        }
    }


    public static void switchPageToRAM(int PNR, int pid) {
        int frameNr = getframe(Plist[pid]);
        // mogelijks werkt dit niet correct
        Plist[pid].addPageToRAM(PNR, frameNr);
        frames[frameNr] = Plist[pid].getPT()[PNR];

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
                if(getFrameIndex(pg) != -1){
                    LRU_time = pg.LAT;
                    LRU_index = getFrameIndex(pg);
                }
            }
        }
        frames[LRU_index].PB = 0;
        p.removePageFromRAM(frames[LRU_index].pageNr);
        frames[LRU_index].frameNumber = -1;

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
}
