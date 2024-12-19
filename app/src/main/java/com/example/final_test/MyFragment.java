package com.example.final_test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyFragment extends Fragment {
    private TextView userInfoTextView;
    private Button logoutButton;
    private Button postButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        userInfoTextView = view.findViewById(R.id.user_info);
        logoutButton = view.findViewById(R.id.log_out);
        postButton = view.findViewById(R.id.post_news);

        // 获取用户名
        SharedPreferences preferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "用户"); // 默认值为“用户”
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        String role = dbHelper.getRoleByUsername(username);
        // 设置文本
        if (role.equals("管理员")){
            userInfoTextView.setText("Hi,管理员: " + username);
            postButton.setVisibility(View.VISIBLE);
        }
        else
            userInfoTextView.setText("Hi,用户: " + username);

        logoutButton.setOnClickListener(v -> {
            // 处理退出登录逻辑
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("is_logged_in", false);
            editor.apply();
            // 返回登录界面
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish(); // 结束当前 Activity
        });

        postButton.setOnClickListener(v ->{
            startActivity(new Intent(getActivity(), PublishNewsActivity.class));
        });

        return view;
    }
}