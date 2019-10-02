package com.example.libraryproject.ui.contact_us;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContactusViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ContactusViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Contact fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}