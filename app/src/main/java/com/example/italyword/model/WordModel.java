package com.example.italyword.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WordModel {

    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo
    public String italyWord;

    @ColumnInfo
    public String englishWord;

    @ColumnInfo
    public String imageUrl;

    @ColumnInfo
    public String imageUrlFireStoreId;

    @ColumnInfo
    public String imageUrlFirebaseId;

    @ColumnInfo
    public String checkStatus ;
}
