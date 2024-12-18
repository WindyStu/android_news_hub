package com.example.final_test;

import android.content.Context;

public class DatabaseGetThread extends Thread{
    private NewsViewModel newsViewModel;
    private Context context;

    public DatabaseGetThread(NewsViewModel viewModel, Context context) {
        this.newsViewModel = viewModel;
        this.context = context;
    }
    @Override
    public void run() {

    }
}
