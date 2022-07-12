package com.example.kb_2022;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
public class Community_Read extends AppCompatActivity {
    private TextView Title;
    private TextView Writer;
    private TextView Content;
    private TextView Like;
    private TextView Number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.community_read);
        Intent intent = getIntent();
        Title = findViewById(R.id.R_title);
        Writer = findViewById(R.id.R_writer);
        Content = findViewById(R.id.R_content);
        Like = findViewById(R.id.R_like);
        Number = findViewById(R.id.R_number);
        Title.setText(intent.getStringExtra("제목"));
        Writer.setText(intent.getStringExtra("글쓴이"));
        Content.setText(intent.getStringExtra("내용"));
        Like.setText(intent.getStringExtra("좋아요"));
        Number.setText(intent.getStringExtra("글 번호"));
    }
}
