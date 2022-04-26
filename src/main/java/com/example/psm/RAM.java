package com.example.psm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalInt;

public class RAM {
    static int aantalProc;
    static Proces[] Ram;
    // totale grootte: 49152

    public RAM() {
        this.aantalProc = 0;
        this.Ram = new Proces[12];
    }

    public static int getAantalProc() {
        return aantalProc;
    }

    public static void setAantalProc(int aantalProc) {
        RAM.aantalProc = aantalProc;
    }

    public static Proces[] getRam() {
        return Ram;
    }

    public static void setRam(Proces[] ram) {
        Ram = ram;
    }

    public ArrayList<Integer> getPIDs(){
        ArrayList<Integer> PIDs = new ArrayList<>(12);
        for(int i=0; i<12; i++){
            PIDs.set(i, Ram[i].getPid());
        }
        return PIDs;
    }

    public boolean addToRAM(Proces p) {
        // Check if still room in RAM
        Proces p2;
        Proces p3;

        if (aantalProc < 4){
            switch (aantalProc){
                case 0:
                    setAantalProc(1);
                    for(int i=0; i<12; i++){
                        Ram[i] = p;
                    }
                    break;

                case 1:
                    setAantalProc(2);
                    for(int i=0; i<6; i++){
                        Ram[i] = Ram[0];
                        Ram[6+i] = p;
                    }
                    break;

                case 2:
                    setAantalProc(3);
                    p2 = Ram[6];
                    for(int i=0; i<4; i++){
                        Ram[i] = Ram[0];
                        Ram[4+i] = p2;
                        Ram[8+i] = p;
                    }
                    break;

                case 3:
                    setAantalProc(4);
                    p2 = Ram[4];
                    p3 = Ram[8];
                    for(int i=0; i<3; i++){
                        Ram[i] = Ram[0];
                        Ram[3+i] = p2;
                        Ram[6+i] = p3;
                        Ram[9+i] = p;
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
                        p2 = Ram[0];
                    } else {
                        p2 = Ram[6];
                    }
                    for(int i=0; i<12; i++){
                        Ram[i] = p2;
                    }
                    break;

                case 3:
                    setAantalProc(2);
                    switch(index){
                        case 0:
                            p2 = Ram[4];
                            p3 = Ram[8];
                            break;


                        case 4:
                            p2 = Ram[0];
                            p3 = Ram[8];
                            break;


                        case 8:
                            p2 = Ram[0];
                            p3 = Ram[4];
                            break;


                        default:
                            throw new RuntimeException("Impossible index of first occurence in RAM for process");
                    }

                    for(int i=0; i<6; i++){
                        Ram[i] = p2;
                        Ram[6+i] = p3;
                    }
                    break;

                case 4:
                    setAantalProc(3);
                    switch(index){
                        case 0:
                            p2 = Ram[3];
                            p3 = Ram[6];
                            p4 = Ram[9];
                            break;


                        case 3:
                            p2 = Ram[0];
                            p3 = Ram[6];
                            p4 = Ram[9];
                            break;


                        case 6:
                            p2 = Ram[0];
                            p3 = Ram[3];
                            p4 = Ram[9];
                            break;

                        case 9:
                            p2 = Ram[0];
                            p3 = Ram[3];
                            p4 = Ram[6];
                            break;

                        default:
                            throw new RuntimeException("Impossible index of first occurence in RAM for process");
                    }

                    for(int i=0; i<4; i++){
                        Ram[i] = p2;
                        Ram[4+i] = p3;
                        Ram[8+i] = p4;
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
        int PID = Ram[frameNr].getPid();

        return PID;
    }
}
