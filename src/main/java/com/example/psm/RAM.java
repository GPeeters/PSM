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
        this.aantalProc = 0;
        this.frames = new Page[12];
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

    public boolean removeFromRAM(Proces p) {
        Proces p2;
        Proces p3;
        Proces p4;

        ArrayList<Integer> PIDs = getPIDs();
        int index = PIDs.indexOf(p.getPid());

        if(index == -1){
            // -1 betekent dat dit proces zich niet in het RAM bevindt
            return false;
        }

        // deze if is in principe niet nodig
        if (aantalProc > 0){
            switch (aantalProc){
                case 1:
                    setAantalProc(0);
                    break;
                    // RAM moet niet leeggemaakt worden tot er een nieuw proces in komt
                case 2:
                    setAantalProc(1);
                    if(index == 6){
                        p2 = frames[0];
                    } else {
                        p2 = frames[6];
                    }
                    for(int i=0; i<12; i++){
                        frames[i] = p2;
                    }
                    break;

                case 3:
                    setAantalProc(2);
                    switch(index){
                        case 0:
                            p2 = frames[4];
                            p3 = frames[8];
                            break;


                        case 4:
                            p2 = frames[0];
                            p3 = frames[8];
                            break;


                        case 8:
                            p2 = frames[0];
                            p3 = frames[4];
                            break;


                        default:
                            throw new RuntimeException("Impossible index of first occurence in RAM for process");
                    }

                    for(int i=0; i<6; i++){
                        frames[i] = p2;
                        frames[6+i] = p3;
                    }
                    break;

                case 4:
                    setAantalProc(3);
                    switch(index){
                        case 0:
                            p2 = frames[3];
                            p3 = frames[6];
                            p4 = frames[9];
                            break;


                        case 3:
                            p2 = frames[0];
                            p3 = frames[6];
                            p4 = frames[9];
                            break;


                        case 6:
                            p2 = frames[0];
                            p3 = frames[3];
                            p4 = frames[9];
                            break;

                        case 9:
                            p2 = frames[0];
                            p3 = frames[3];
                            p4 = frames[6];
                            break;

                        default:
                            throw new RuntimeException("Impossible index of first occurence in RAM for process");
                    }

                    for(int i=0; i<4; i++){
                        frames[i] = p2;
                        frames[4+i] = p3;
                        frames[8+i] = p4;
                    }
                    break;

                default:
                    throw new RuntimeException("Amount of processes in RAM is negative. This should not be possible.");
            }
            return true;
        } else {
            return false;
        }
    }

    public int addressToPID(int address){
        int frameNr = (int) Math.floor(address/4096);
        int PID = frames[frameNr].getPID();

        return PID;
    }

    public int getframe(Proces p) {
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
        int allocatedframe = frames[LRU_index].pageNr;
        frames[LRU_index].frameNum = -1;

        //return the number of the allocated frame
        return allocatedframe;
    }
}
