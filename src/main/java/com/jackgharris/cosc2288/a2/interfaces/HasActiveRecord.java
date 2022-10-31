package com.jackgharris.cosc2288.a2.interfaces;

import com.jackgharris.cosc2288.a2.models.Record;

public interface HasActiveRecord {

    void setRecord(Record record);
    Record getRecord();
}
