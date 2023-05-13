package com.example.italyword.model;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.italyword.AppDatabase;

import java.util.List;

public class Forgotablerepository {

    private AppDatabase db;
    private ForgatableDao forgatableDao;

    private MutableLiveData<String> returnResult =  new MutableLiveData<>();
    private ForgatableWordModel forgatableWordModelMutableLiveData;
    private Handler handler = new Handler();
    private String tag = this.getClass().getSimpleName();
    private LifecycleOwner owner;

    private MutableLiveData<List<WordModel>> wordList = new MutableLiveData<>();
    public Forgotablerepository(Application application) {

        db =AppDatabase.getInstance(application);
        forgatableDao =db.forgatableDao();
        forgatableWordModelMutableLiveData = new ForgatableWordModel();
    }

    public MutableLiveData<String> insertForgotbleWord(ForgatableWordModel model){



        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                ForgatableWordModel forgatableWordModel=  forgatableDao.findWorder(model.wordId);
                if(forgatableWordModel !=null){
                    model.id =forgatableWordModel.id;
                    model.forgotableCount = forgatableWordModel.forgotableCount+1;
                    if(forgatableWordModel.id !=null){
                        Log.d(tag,"forgotbale found fogotableid { "+model.id+"} wordid {"+model.wordId+"} cout {"+model.forgotableCount+"}");
                        forgatableDao.updateData(model);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(tag,"forgotbale found 2");
                                returnResult.setValue("Success");
                                Log.d(tag,"forgotbale found"+"::"+"success> "+model.forgotableCount);
                            }
                        });
                    }
                }else{
                    Log.d(tag," not forgotbale found");
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            model.forgotableCount =1;
                            forgatableDao.insertData(model);
                            Log.d(tag," not forgotbale found"+"::"+"count is in  null -> "+model.forgotableCount);
                            Log.d(tag," not forgotbale found 1");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    returnResult.setValue("Success");
                                    Log.d(tag," not forgotbale found 2");
                                }
                            });
                        }
                    });
                }
            }
        });

        return returnResult;
    }

    public ForgatableWordModel findByWordId(int id)  {
       return forgatableDao.findWorder(id);
    }

    public MutableLiveData<List<WordModel>> getForgotableWordList(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<WordModel> wordlst = forgatableDao.getForgotableWordlist();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        wordList.setValue(wordlst);
                    }
                });


            }
        });

        return wordList;
    }

    public MutableLiveData<String> deleteForgotableWord(int id){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ForgatableWordModel model = null;
                    model = findByWordId(id);
                if(model !=null){
                    Log.d(tag,"findby id in delete !nulll -> "+model.wordId);
                        forgatableDao.delete(model);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                returnResult.setValue("deletd");
                            }
                        });
                }else{
                    Log.d(tag,"findby id in delete nulll");
                }
            }
        });
        return returnResult;
    }
 }
