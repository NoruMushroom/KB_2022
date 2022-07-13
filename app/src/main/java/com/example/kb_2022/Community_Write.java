package com.example.kb_2022;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Community_Write extends AppCompatActivity {
    private EditText Title;
    private EditText Content;
    private EditText PW;
    private Button Save;
    private Context This_Activity;
    private String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.community_write);
        Title = findViewById(R.id.W_title);
        Title.setSelection(0);
        Content = findViewById(R.id.W_content);
        Content.setSelection(0);
        PW = findViewById(R.id.W_pw);
        PW.setSelection(0);
        Save = findViewById(R.id.W_save);
    }
}