package com.example.italyword.model;

import android.app.Application;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.italyword.AppDatabase;
import com.example.italyword.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class wordfbRepository {

    private AppDatabase db;
    private WordDetailsDao wordDetailsDao;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseStorage storage;
    private StorageReference storageRef;
    private MutableLiveData<String> resultReturn;

    private Handler handler = new Handler();
    private String tag = this.getClass().getName();

    private MutableLiveData<List<WordModel>> wordModelList;
    private List<WordModel> list = new ArrayList<>();
    private WordModel wordModel = new WordModel();

    private MutableLiveData<WordModel> wordModelMutableLiveData;

    private String fireBaseuser = "";

    private MutableLiveData<String> checkStatusCount = new MutableLiveData<>();

    public wordfbRepository(Application application) {

        fireBaseuser = application.getString(R.string.Username);
        this.db = AppDatabase.getInstance(application);
        this.wordDetailsDao = db.wordDao();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        resultReturn = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ItalyWord").child(fireBaseuser);
        wordModelList = new MutableLiveData<>();
        wordModelMutableLiveData = new MutableLiveData<>();

    }


    public MutableLiveData<String> getCheckStatusCount() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Integer done = wordDetailsDao.getCheckStatusDone();
                Integer wordCount = wordDetailsDao.getallWordCount();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        checkStatusCount.setValue(done.toString() + "/" + wordCount.toString());
                    }
                });
            }
        });

        return checkStatusCount;

    }

    public void makewordDone(String status, int wordid) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                WordModel model = findByWordId(wordid);
                if (model != null) {
                    Log.d(tag, "check done updatd -> " + model.id + "{---}" + wordid);
                    wordDetailsDao.updateChakeStatus(status, wordid);
                } else {
                    Log.d(tag, "check done not updatd -> ");
                }
            }
        });

    }

    public void resetAllWordStatus() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                wordDetailsDao.resetAllChakeStatus();
                Log.d(tag, "all reset ");
            }
        });
    }

    public LiveData<String> uplaodeData(WordModel model) {

        if (model.id == null) {
            if (model.imageUrl.equals("https://firebasestorage.googleapis.com/v0/b/italyword.appspot.com/o/empty%2Fdownload.png?alt=media&token=67bd13c1-0976-4d09-9dbe-460f7d245be7")) {
                String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/italyword.appspot.com/o/empty%2Fdownload.png?alt=media&token=67bd13c1-0976-4d09-9dbe-460f7d245be7";
                //insert
                model.imageUrl = downloadUrl;
                model.imageUrlFireStoreId = "noId";
                ItalyWordFbModel wordfb = new ItalyWordFbModel();
                wordfb.imageUrl = model.imageUrl;
                wordfb.englishWord = model.englishWord;
                wordfb.italyWord = model.italyWord;
                model.checkStatus = "notdone";
                saveDataOnRealTime(wordfb, model);
            } else {
                //insert
                String ImageFireStoreId = model.englishWord + UUID.randomUUID().toString();

                StorageReference riversRef = storageRef.child("italyWord").child(fireBaseuser).child("images").child(ImageFireStoreId);

                riversRef.putFile(Uri.parse(model.imageUrl.toString())).addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String downloadUrl = uri.toString();

                                        model.imageUrl = downloadUrl;
                                        model.imageUrlFireStoreId = ImageFireStoreId;
                                        ItalyWordFbModel wordfb = new ItalyWordFbModel();
                                        wordfb.imageUrl = model.imageUrl;
                                        wordfb.englishWord = model.englishWord;
                                        wordfb.italyWord = model.italyWord;
                                        model.checkStatus = "notdone";
                                        saveDataOnRealTime(wordfb, model);
                                        // saveinRoom(model);


                                    }
                                });
                            }
                        }
                );
            }


        } else {

            if (model.imageUrl != null && model.imageUrl.contains("https://firebasestorage.googleapis.com")) {
                //update
                updateSqlit(model);
            } else {
                if (model.imageUrl.equals("https://firebasestorage.googleapis.com/v0/b/italyword.appspot.com/o/empty%2Fdownload.png?alt=media&token=67bd13c1-0976-4d09-9dbe-460f7d245be7")) {
                    String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/italyword.appspot.com/o/empty%2Fdownload.png?alt=media&token=67bd13c1-0976-4d09-9dbe-460f7d245be7";

                    model.imageUrl = downloadUrl;
                    model.imageUrlFireStoreId = "noId";

                    updateSqlit(model);
                } else {
                    StorageReference riversRef = storageRef.child("italyWord").child(fireBaseuser).child("images").child(model.imageUrlFireStoreId);

                    riversRef.putFile(Uri.parse(model.imageUrl.toString())).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            String downloadUrl = uri.toString();

                                            model.imageUrl = downloadUrl;
                                            model.imageUrlFireStoreId = model.imageUrlFireStoreId;

                                            updateSqlit(model);
                                            // saveinRoom(model);


                                        }
                                    });
                                }
                            }
                    );
                }


            }

        }


        return resultReturn;

    }

    public LiveData<String> deleteItem(WordModel model) {

        StorageReference riversRef = storageRef.child("italyWord").child(fireBaseuser).child("images").child(model.imageUrlFireStoreId);
        riversRef.delete();
        databaseReference.child(model.imageUrlFirebaseId).removeValue();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                WordModel word = wordDetailsDao.findById(model.id);
                if (word != null) {
                    wordDetailsDao.deleteWord(word);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            resultReturn.setValue("deleted success");
                        }
                    });

                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            resultReturn.setValue("deleted faile");
                        }
                    });

                }
            }
        });


        return resultReturn;
    }


    private void updateSqlit(WordModel model) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                wordDetailsDao.updateWord(model);
                ItalyWordFbModel italyWordFbModel = new ItalyWordFbModel();
                italyWordFbModel.italyWord = model.italyWord;
                italyWordFbModel.englishWord = model.englishWord;
                italyWordFbModel.imageUrl = model.imageUrl;
                databaseReference.child(model.imageUrlFirebaseId).setValue(italyWordFbModel);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resultReturn.setValue("success");
                    }
                });
            }
        });
    }

    private void saveDataOnRealTime(ItalyWordFbModel italyWordFbModel, WordModel model) {

        String firebaseid = databaseReference.push().getKey();
        model.imageUrlFirebaseId = firebaseid;
        databaseReference.child(firebaseid).setValue(italyWordFbModel);
        saveinRoom(model);
    }


    private void saveinRoom(WordModel model) {

        if (model != null && model.imageUrl.length() != 0 && model.italyWord.length() != 0 && model.englishWord.length() != 0) {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    wordDetailsDao.insertWord(model);
                    try {


                        Log.d(tag, "room  excuted");

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                resultReturn.setValue("success");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(tag, "room  not excuted -> " + e.getMessage());
                    }


                }
            });

        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    resultReturn.setValue("fill All value");
                }
            });

        }


    }

    public MutableLiveData<List<WordModel>> getallValueList() {

        getallValueListPaginate(0);
        getallValueListPaginate(10);
        getallValueListPaginate(20);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                list = wordDetailsDao.getAllWordRandom();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        wordModelList.postValue(list);
                    }
                });
                Log.d(tag, "value ");
            }
        });

        return wordModelList;
    }

    private WordModel findByWordId(int Wordid) {

        return wordDetailsDao.findById(Wordid);
    }


    public MutableLiveData<WordModel> findByID(int id) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {


                wordModel = wordDetailsDao.findById(id);


                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (wordModel != null) {
                            wordModelMutableLiveData.setValue(wordModel);
                        }
                    }
                });

            }
        });

        return wordModelMutableLiveData;
    }

    public void deletImageInfireBaseBy(String id) {
        StorageReference riversRef = storageRef.child("italyWord").child(fireBaseuser).child("images");
        if (riversRef.child(id) != null) {
            riversRef.child(id).delete();
            Log.d(tag, "imageDeleted");
        } else {
            Log.d(tag, "imageDeleted");
        }

    }

    public MutableLiveData<List<WordModel>> getallValueListPaginate(Integer offset) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {


                list = wordDetailsDao.getAllWordRandomPaginate(offset);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        wordModelList.postValue(list);
                    }
                });

            }
        });

        return wordModelList;


    }

    public MutableLiveData<List<WordModel>> getaSearchWords(String searchingKey) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                list = wordDetailsDao.getSearchWords(searchingKey);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        wordModelList.postValue(list);
                    }
                });

            }
        });

        return wordModelList;


    }
}
