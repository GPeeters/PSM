package com.example.psm;

import java.util.ArrayList;

public class HardDrive{
    ArrayList<Integer> PIDlist;

    public HardDrive(){
        PIDlist = new ArrayList<>(16);
        for (int j = 0; j < 16; j++) {
            PIDlist.add(j, -1);
        }
    }

    //zodat het makkelijk in de GUI kan worden weergeven
    public void setPage(int PID, int add){
        int page = add/4096;
        if(PIDlist.get(page)==-1){
            PIDlist.set(page, PID);
        }
    }

    public void clearProc(int PID){
        for (int i: PIDlist) {
            if(PIDlist.get(i) == PID){
                PIDlist.set(i, -1);
            }
        }
    }
}


