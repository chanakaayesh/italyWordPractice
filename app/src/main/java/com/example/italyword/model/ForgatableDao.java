package com.example.italyword.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ForgatableDao {

    @Query("SELECT* FROM  ForgatableWordModel" )
    ForgatableWordModel getWords();

    @Insert
    void insertData(ForgatableWordModel model);


    @Update
    void updateData(ForgatableWordModel model);

    @Query("SELECT* FROM ForgatableWordModel ")
    List<ForgatableWordModel> getAllForgatbleWords();

    @Query("SELECT * FROM ForgatableWordModel WHERE wordId=(:id)")
    ForgatableWordModel findWorder(int id);

    @Query("SELECT W.id,W.englishWord,W.italyWord,W.imageUrl,W.imageUrlFirebaseId,W.imageUrlFireStoreId,W.checkStatus  FROM ForgatableWordModel A ,WordModel W  WHERE A.wordId= W.id")
    List<WordModel> getForgotableWordlist();

    @Delete()
    void delete(ForgatableWordModel model);

}
