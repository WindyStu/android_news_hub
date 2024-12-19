package com.example.final_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.ViewGroup;


import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {
    public CommentAdapter(Context context, List<Comment> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
        }

        TextView commentText = convertView.findViewById(R.id.comment_text);
        TextView commentTime = convertView.findViewById(R.id.comment_time);
        TextView commentUser = convertView.findViewById(R.id.comment_user);

        commentText.setText(comment.getContent());
        commentTime.setText(comment.getTimestamp());
        commentUser.setText(comment.getUsername());

        return convertView;
    }
}
