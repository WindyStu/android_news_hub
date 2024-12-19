package com.example.final_test;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public CustomSpinnerAdapter(Context context, String[] values) {
        super(context, android.R.layout.simple_spinner_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;
        textView.setTextColor(context.getResources().getColor(R.color.black)); // 设置下拉项颜色
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "title.ttf")); // 设置字体
        textView.setTextSize(20);
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;
        textView.setTextColor(context.getResources().getColor(R.color.black)); // 设置选中项颜色
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "title.ttf")); // 设置字体
        textView.setTextSize(20);
        return view;
    }
}
