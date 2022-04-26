package com.example.psm;

public class Instructie {
    int pid;        //ProcessID
    String op;      //Operation
    int add;        //Address

    public Instructie(int id, String op, int add) {
        this.pid = id;
        this.op = op;
        this.add = add;
    }
}
