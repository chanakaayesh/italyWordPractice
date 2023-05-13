package com.example.italyword.view.forgetableWord;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForgotableViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ForgotableViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}