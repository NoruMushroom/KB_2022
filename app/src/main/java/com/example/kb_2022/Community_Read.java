package com.example.kb_2022;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
public class Community_Read extends AppCompatActivity {
    private TextView Title;
    private TextView Writer;
    private TextView Content;
    private TextView Like;
    private ImageButton Like_Btn;
    private ImageButton Bad_Btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.community_read);
        Intent intent = getIntent();
        Like_Btn = findViewById(R.id.image_like);
        Bad_Btn = findViewById(R.id.image_unlike);
        Title = findViewById(R.id.R_title);
        Writer = findViewById(R.id.R_writer);
        Content = findViewById(R.id.R_content);
        Like = findViewById(R.id.R_like);
        String number = intent.getStringExtra("글 번호");//string형 글번호 변수
        String like = intent.getStringExtra("좋아요");
        String writer = intent.getStringExtra("글쓴이");
        Title.setText(intent.getStringExtra("제목"));
        Content.setText(intent.getStringExtra("내용"));
        Like.setText("좋아요 "+like);
        Writer.setText("글쓴이:" + writer);
        Like_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        Bad_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
