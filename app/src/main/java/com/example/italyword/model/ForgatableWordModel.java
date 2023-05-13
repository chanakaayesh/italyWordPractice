package com.example.italyword.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ForgatableWordModel {

    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo
    public int wordId;

    @ColumnInfo
    public Integer forgotableCount;



}
