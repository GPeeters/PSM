package com.example.psm;

import java.util.ArrayList;

public class HardDrive{
    ArrayList<Proces> Plist;

    public HardDrive(){
        Plist = new ArrayList<>(20);
        for (int j = 0; j < 20; j++) {
            Plist.add(new Proces(j));
        }
    }
}


