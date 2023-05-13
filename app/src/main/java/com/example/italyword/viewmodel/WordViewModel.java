package com.example.italyword.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.italyword.model.WordModel;
import com.example.italyword.model.wordfbRepository;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    MutableLiveData<WordModel> wordModel=new MutableLiveData<>();
   static wordfbRepository wordRepository;
   public LiveData setModelForsaveData ;
   LiveData<String> saveDataInSql;
   LiveData<String> imageUrl ;
   MutableLiveData<List<WordModel>> woListMutableLiveDataList;
    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new wordfbRepository(application);
        imageUrl = Transformations.switchMap(wordModel,WordViewModel::saveData);
        woListMutableLiveDataList = new MutableLiveData<>();
    }

    public MutableLiveData<String> getCheckStatusCount(){
        return wordRepository.getCheckStatusCount();
    }

    public static LiveData<String> saveData(WordModel model){
      return   wordRepository.uplaodeData(model);
    }

    public void deleteImage(String id){
        wordRepository.deletImageInfireBaseBy(id);
    }

    public MutableLiveData<List<WordModel>> getAllValue(){
        return wordRepository.getallValueList();
    }

    public MutableLiveData<List<WordModel>> getValueListPaging(Integer offset){
        return wordRepository.getallValueListPaginate(offset);
    }
    public void searchWords(String key){
       wordRepository.getaSearchWords(key);
    }
    public void updateWordCheckStatus(String status,int wordId){
        wordRepository.makewordDone(status,wordId);
    }
    public void resetAllWordStatus(){
        wordRepository.resetAllWordStatus();
    }
    public MutableLiveData<WordModel> findById(int id){
        return wordRepository.findByID(id);
    }
    public LiveData<String> deleteWord(WordModel model){
        return wordRepository.deleteItem(model);
    }
    public LiveData<String> getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(WordModel wordModell) {
        wordModel.setValue(wordModell);
    }

}
