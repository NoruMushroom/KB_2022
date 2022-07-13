package com.example.kb_2022;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
    private String userName;
    private Context This_Activity;
    private String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.community_write);
        Intent intent = getIntent();
        userName = intent.getStringExtra("이름");
        Title = findViewById(R.id.W_title);
        Title.setSelection(0);
        PW = findViewById(R.id.W_pw);
        PW.setSelection(0);
        Content = findViewById(R.id.W_content);
        Content.setSelection(0);
        Save = findViewById(R.id.W_save);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData task = new GetData();
                task.execute("writeboard", Title.getText().toString(), userName, PW.getText().toString(), Content.getText().toString());
            }
        });
    }
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Community_Write.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();


            if (result == null){
                Log.d(TAG, "response - " + result);
            }
            else {
                mJsonString = result;
                showResult();
            }

        }
        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://123.215.162.92/KBServer/" + params[0] + ".php";
            String title = params[1];
            String uname = params[2];
            String uPassword = params[3];
            String content = params[4];

            String postParameters = "title=" + title + "&uname=" + uname + "&upw=" + uPassword + "&content=" + content;


            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "응답코드 : " + responseStatusCode);
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                errorString = e.toString();
                return null;
            }
        }
    }
    private void showResult() {
        String TAG_SUCCESS = "success";
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            String success = jsonObject.getString(TAG_SUCCESS);
            System.out.println(success);

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}