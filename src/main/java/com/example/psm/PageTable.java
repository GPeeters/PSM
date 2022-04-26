package com.example.psm;

import java.util.ArrayList;

public class PageTable {
    Page zeroPage = new Page();
    private Page[] PT;

    PageTable() {
        this.PT = new Page[16];
        for(int i=0; i<16; i++){
            PT[i] = new Page(0, 0, 0, i);
        }
    }

    public Page getPage(int nr) {
        if(0 <= nr && nr < 16){
            return PT[nr];
        } else {
            throw new RuntimeException("Page number out of bounds [0, 15]");
        }
    }

    public int addressToPageNr(int address){
        int PageNr = (int) Math.floor(address/4096);
        return PageNr;
    }
}
