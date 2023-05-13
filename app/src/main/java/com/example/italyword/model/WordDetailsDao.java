package com.example.italyword.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.italyword.model.WordModel;

import java.util.HashMap;
import java.util.List;

@Dao
public interface WordDetailsDao {

    @Query("SELECT* FROM wordmodel")
    List<WordModel> getAllWord();

    @Insert
    void insertWord(WordModel wordModel);

    @Query("SELECT* FROM wordmodel WHERE id=(:ids)")
    WordModel findById(int ids);

    @Query("SELECT* FROM wordmodel ")
    List<WordModel> getAllWordRandom();

    @Query("SELECT* FROM wordmodel where checkStatus='notdone' ORDER BY RANDOM() LIMIT (:offset),5 ")
    List<WordModel> getAllWordRandomPaginate(Integer offset);

    @Query("SELECT* FROM wordmodel where italyWord LIKE  '%'||:searchKey||'%' or englishWord LIKE  '%'||:searchKey||'%' ")
    List<WordModel> getSearchWords(String searchKey);

    @Update
    void updateWord(WordModel modl);

    @Delete
    void deleteWord (WordModel modl);

    @Query("UPDATE WordModel SET checkStatus =(:status) WHERE id=(:id)")
    void updateChakeStatus(String status,int id);
    @Query("UPDATE WordModel SET checkStatus ='notdone'WHERE checkStatus='done'")
    void resetAllChakeStatus();

    @Query("SELECT count(checkStatus) FROM wordmodel where checkStatus='done' ")
    Integer getCheckStatusDone();

    @Query("SELECT count(checkStatus) FROM wordmodel  ")
    Integer getallWordCount();



}
