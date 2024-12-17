package com.example.final_test;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewsViewModel extends ViewModel {
    private MutableLiveData<List<NewsItem>> newsList;
    private MutableLiveData<String> errMessage = new MutableLiveData<>();

    public NewsViewModel() {
        newsList = new MutableLiveData<>();
        newsList.setValue(new ArrayList<>());
    }

    public MutableLiveData<List<NewsItem>> getNewsList() {
        return newsList;
    }

    public MutableLiveData<String> getErrMessage() {
        return errMessage;
    }
}
