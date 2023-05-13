package com.example.italyword.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.italyword.AppDatabase;
import com.example.italyword.model.ForgatableWordModel;
import com.example.italyword.model.Forgotablerepository;
import com.example.italyword.model.WordModel;

import java.util.List;

public class ForgatableViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private MutableLiveData<ForgatableWordModel> modelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> modelMutableLiveDataWordID = new MutableLiveData<>();
    private  static  Forgotablerepository forgotablerepository;
    private LiveData<String> insertForgotable  = Transformations.switchMap(modelMutableLiveData,ForgatableViewModel::insertData);

    public ForgatableViewModel(@NonNull Application application) {
        super(application);
        forgotablerepository = new Forgotablerepository(application);
    }


    public static MutableLiveData<String> insertData(ForgatableWordModel model){
        return forgotablerepository.insertForgotbleWord(model);
    }

    public MutableLiveData<String> deleteForgotableWord(int model){
        return  forgotablerepository.deleteForgotableWord(model);
    }

    public MutableLiveData<List<WordModel>> getFortableAllWordList(){

        return forgotablerepository.getForgotableWordList();
    }

    public LiveData<String> getInsertForgotable() {
        return insertForgotable;
    }
    public void setInsertForgotable(ForgatableWordModel insertForgotable) {
        modelMutableLiveData.setValue(insertForgotable);
    }

}
