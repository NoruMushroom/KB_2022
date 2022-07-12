package com.example.kb_2022;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class Community_Read extends AppCompatActivity {
    private TextView Title;
    private TextView Writer;
    private TextView Content;
    private TextView Like;
    private ImageButton Like_Btn;
    private ImageButton Bad_Btn;
    private Context This_Activity;
    private String mJsonString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        This_Activity = getApplicationContext();
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
        Writer.setText("글쓴이 : " + writer);
        Like_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData task = new GetData();
                task.execute("http://123.215.162.92/KBServer/likeupdown.php", number , "true");
            }
        });
        Bad_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    private class GetData extends AsyncTask<String, Void, String> {

        //ProgressDialog progressDialog;
        String errorString = null;

        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(This_Activity,
                    "Please Wait", null, true, true);
        }*/


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.dismiss();
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
            String serverURL = params[0];
            String Bno = params[1];
            String isLike = params[2];
            String postParameters = "bno=" + Bno + "&islike=" + isLike;
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
