//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.models;

//**** START RECORD TYPE CLASS ***\\
public class RecordType {

    //**** CLASS VARIABLES ****\\

    //count, this tracks the count of the records for this type
    private final int count;

    //name, this tracks the name of this record type
    private final String name;

    //**** CONSTRUCTOR ****\\
    //Sets the class variables.
    public RecordType(String name, int count){
        this.name = name;
        this.count = count;
    }

    //**** GET NAME METHOD ****\\
    //returns the name of this record type
    public String getName(){
        return this.name;
    }

    //**** GET COUNT METHOD ****\\
    //returns the record count for this record type
    public int getCount(){
        return this.count;
    }

}
