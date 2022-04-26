package com.example.psm;

import java.util.ArrayList;

import static com.example.psm.PSMApplication.Plist;

public class RAM {
    static int aantalProc;
    static Page[] frames;
    ArrayList<Integer> procInRam;
    // totale grootte: 49152
    // grootte van 1 frame: 4096

    public RAM() {
        aantalProc = 0;
        frames = new Page[12];
        this.procInRam = new ArrayList<>();
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

        if (aantalProc < 4){
            switch (aantalProc){
                case 0:
                    setAantalProc(1);
                    for(int i=0; i<12; i++){
                        frames[i] = p.PT[i];
                        p.addPageToRAM(i);
                        procInRam.add(p.getPid());
                    }
                    break;

                case 1:
                    setAantalProc(2);
                    p2 = Plist[procInRam.get(0)];
                    for(int i=0; i<6; i++){
                        int released = getframe(p2);
                        frames[released] = p.PT[i];
                        p.addPageToRAM(i);
                    }
                    procInRam.add(p.getPid());
                    break;

                case 2:
                    setAantalProc(3);
                    p2 = Plist[procInRam.get(0)];
                    p3 = Plist[procInRam.get(1)];
                    for(int i=0; i<2; i++){
                        int r1 = getframe(p2);
                        int r2 = getframe(p3);
                        frames[r1] = p.PT[i];
                        frames[r2] = p.PT[i+2];
                        p.addPageToRAM(i);
                        p.addPageToRAM(i+2);
                    }
                    procInRam.add(p.getPid());
                    break;

                case 3:
                    setAantalProc(4);
                    p2 = Plist[procInRam.get(0)];
                    p3 = Plist[procInRam.get(1)];
                    p4 = Plist[procInRam.get(2)];

                    int r1 = getframe(p2);
                    int r2 = getframe(p3);
                    int r3 = getframe(p4);

                    frames[r1] = p.PT[0];
                    frames[r2] = p.PT[1];
                    frames[r3] = p.PT[2];
                    p.addPageToRAM(0);
                    p.addPageToRAM(1);
                    p.addPageToRAM(2);

                    procInRam.add(p.getPid());
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
                    procInRam.remove(p.getPid());
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
                        frames[pnr] = p2.PT[p2.addPageToRAM()];
                    }

                    procInRam.remove(p.getPid());
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
                        frames[pnr] = p2.PT[p2.addPageToRAM()];
                    }

                    for(int i=0;i<2;i++){
                        int pnr =  getframe(p);
                        frames[pnr] = p3.PT[p3.addPageToRAM()];
                    }

                    procInRam.remove(p.getPid());
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

                    frames[getframe(p)] = p2.PT[p2.addPageToRAM()];
                    frames[getframe(p)] = p3.PT[p3.addPageToRAM()];
                    frames[getframe(p)] = p3.PT[p3.addPageToRAM()];

                    procInRam.remove(p.getPid());
                    break;

                default:
                    throw new RuntimeException("Amount of processes in RAM is negative. This should not be possible.");
            }
        }
    }


    public static void switchPageToRAM(int PNR, int pid) {
        frames[getframe(Plist[pid])] = Plist[pid].PT[PNR];
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
        for (Page pg : p.framesInRam) {
            if(pg.LAT < LRU_time){
                LRU_time = pg.LAT;
                LRU_index = pg.getFrameNum();
            }
        }
        frames[LRU_index].PB = 0;
        p.removePageFromRAM(frames[LRU_index].pageNr);
        frames[LRU_index].frameNum = -1;

        //return the number of the allocated frame
        return LRU_index;
    }
}
