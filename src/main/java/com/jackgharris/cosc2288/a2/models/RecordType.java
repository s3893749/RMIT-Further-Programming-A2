package com.jackgharris.cosc2288.a2.models;

public class RecordType {

    private final int count;
    private final String name;

    public RecordType(String name, int count){
        this.name = name;
        this.count = count;
    }

    public String getName(){
        return this.name;
    }

    public int getCount(){
        return this.count;
    }

}
