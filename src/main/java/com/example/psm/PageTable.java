package com.example.psm;

import java.util.ArrayList;
import java.util.Arrays;

public class PageTable {
    Page zeroPage = new Page();
    private Page[] PT;
    private ArrayList<Integer> filled;

    PageTable() {
        this.PT = new Page[16];
        for(int i=0; i<16; i++){
            PT[i] = zeroPage;
        }
        this.filled = new ArrayList<>();
    }

    public Page getPage(int nr) {
        if(0 <= nr && nr < 16){
            return PT[nr];
        } else {
            throw new RuntimeException("Page number out of bounds [0, 15]");
        }
    }

    // controleert en plaatst entry waar er plek vrij is, als er nog plek is
    public boolean addEntry(Page entry) {
        for(int i=0; i<16; i++){
            if(!filled.contains(i)){
                filled.add(i);
                PT[i] = entry;
                return true;
            }
        }
        return false;
    }

    // zoekt de gegeven entry en verwijdert de entry op de corresponderende plaats
    public void removeEntry(Page entry) {
        int index = entry.getPageNr();

        if(filled.contains(index)){
            filled.remove(index);
            PT[index] = zeroPage;
        } else {
            throw new RuntimeException("Page not found in PT");
        }
    }
}
