package com.example.kb_2022;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Community_Read extends AppCompatActivity {
    private TextView Title;
    private TextView Writer;
    private TextView Content;
    private TextView Like;
    private ImageButton Like_Btn;
    private ImageButton Bad_Btn;
    private ImageButton Delete_Btn;
    private String PW_content;
    private String mJsonString;
    private Integer postValue;
    private GetData D_task;
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
        Delete_Btn = findViewById(R.id.R_delete);
        String number = intent.getStringExtra("글 번호");//string형 글번호 변수
        //글 내용 가져오기
        GetData task = new GetData();
        task.execute("readtext", number);
        Like_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData task = new GetData();
                task.execute("likeupdown", number , "true");
                Intent intent = getIntent();
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        Bad_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData task = new GetData();
                task.execute("likeupdown", number , "false");
                Intent intent = getIntent();
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        Delete_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Community_Read.this);
                builder.setTitle("글 삭제");
                builder.setMessage("\n비밀번호를 입력해주세요.\n");
                final EditText PW = new EditText(Community_Read.this);
                final ConstraintLayout container = new ConstraintLayout(Community_Read.this);
                final ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.alert_dialog_internal_margin);
                params.rightMargin =getResources().getDimensionPixelSize(R.dimen.alert_dialog_internal_margin);
                PW.setLayoutParams(params);
                PW.setBackgroundResource(R.drawable.round_wiget);
                container.addView(PW);
                builder.setView(container);
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PW_content = PW.getText().toString();
                        D_task = new GetData();
                        try {
                            String result = D_task.execute("deletetext", number, PW_content).get();
                            JSONObject j_result = new JSONObject(result);
                            result = j_result.getString("success");//성공 여부
                            if(result.equals("true")){
                                finish();
                                overridePendingTransition(0, 0);
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Community_Read.this,
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
                if(postValue == 1){
                    showContent();
                }
                else if(postValue == 2){
                    showLike();
                }
                else{
                    showDelete();
                }
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://123.215.162.92/KBServer/" + params[0] + ".php";
            String postParameters;
            String Bno;
            String isLike;
            String bpw;
            switch (params[0]){
                case "readtext":
                    Bno = params[1];
                    postParameters = "bno=" + Bno;
                    postValue = 1;
                    break;
                case "likeupdown":
                    Bno = params[1];
                    isLike = params[2];
                    postParameters = "bno=" + Bno + "&islike=" + isLike;
                    postValue = 2;
                    break;
                case "deletetext":
                    Bno = params[1];
                    bpw = params[2];
                    postParameters = "bno=" + Bno + "&bpw=" + bpw;
                    postValue = 3;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + params[0]);
            }

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
    private void showLike() {
        String TAG_SUCCESS = "success";
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            String success = jsonObject.getString(TAG_SUCCESS);
            System.out.println(success);

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
    private void showDelete() {
        String TAG_SUCCESS = "success";
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            String success = jsonObject.getString(TAG_SUCCESS);
            System.out.println(success);

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
    private void showContent(){
        String TAG_JSON = "select_content";
        String TAG_TITLE = "title";
        String TAG_NAME = "name";
        String TAG_CONTENT = "content";
        String TAG_ISLIKE = "islike";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String title = item.getString(TAG_TITLE);
                String name = item.getString(TAG_NAME);
                String content = item.getString(TAG_CONTENT);
                String like = item.getString(TAG_ISLIKE);
                Title.setText(title);
                Writer.setText(name);
                Content.setText(content);
                Like.setText("좋아요 : "+ like);
                }

            }
        catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
