package com.example.kb_2022;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SignUpActivity extends AppCompatActivity {
    private EditText id_ed;
    private EditText pw_ed;
    private EditText name_ed;
    private String gender;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_signup);
        id_ed = findViewById(R.id.ID);
        pw_ed = findViewById(R.id.PW);
        name_ed = findViewById(R.id.Name);
        Button signup_btn = (Button) findViewById(R.id.Signup_request);
        Spinner gender_sp = findViewById(R.id.Gender);
        signup_btn.setEnabled(false);
        //성별 스피너
        gender_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    gender = "남성";
                }
                else{
                    gender = "여성";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //회원가입 버튼
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = id_ed.getText().toString();
                String userPW = pw_ed.getText().toString();
                String userName = name_ed.getText().toString();

                GetData task = new GetData();
                try {
                    String result = task.execute("signup", userID, userPW, userName, gender).get();
                    JSONObject j_result = new JSONObject(result);
                    result = j_result.getString("success");//성공 여부
                    AlertDialog.Builder bulider = new AlertDialog.Builder(SignUpActivity.this);
                    if(result.equals("true")){
                        bulider.setTitle("회원가입 성공");
                        bulider.setMessage("회원가입을 완료하였습니다.");
                        bulider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();//인텐트 종료
                                overridePendingTransition(0, 0);//인텐트 효과 없애기
                            }
                        });
                    }
                    else{
                        bulider.setTitle("회원가입 실패");
                        bulider.setMessage("\n중복된 사용자가 있습니다.\n");
                        bulider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                    }
                    bulider.show();
                } catch (ExecutionException | JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        id_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(id_ed.length()>0){
                    signup_btn.setEnabled(true);
                }
                else{
                    signup_btn.setEnabled(false);
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(id_ed.length()>0){
                    signup_btn.setEnabled(true);
                }
                else{
                    signup_btn.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(id_ed.length()>0){
                    signup_btn.setEnabled(true);
                }
                else{
                    signup_btn.setEnabled(false);
                }
            }
        });
        pw_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(pw_ed.length() > 0){
                    signup_btn.setEnabled(true);
                }
                else {
                    signup_btn.setEnabled(false);
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(pw_ed.length() > 0){
                    signup_btn.setEnabled(true);
                }
                else {
                    signup_btn.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(pw_ed.length() > 0){
                    signup_btn.setEnabled(true);
                }
                else {
                    signup_btn.setEnabled(false);
                }
            }
        });
        name_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(name_ed.length() > 0){
                    signup_btn.setEnabled(true);
                }
                else{
                    signup_btn.setEnabled(false);
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(name_ed.length() > 0){
                    signup_btn.setEnabled(true);
                }
                else{
                    signup_btn.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(name_ed.length() > 0){
                    signup_btn.setEnabled(true);
                }
                else{
                    signup_btn.setEnabled(false);
                }
            }
        });

    }

    //Task 클래스
    private class GetData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignUpActivity.this,
                    "잠시만 기다려주세요", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();


            if (result == null){
                Log.d(TAG, "response - " + result);
            }

        }
        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://123.215.162.92/KBServer/" + params[0] + ".php";
            String postParameters = "userID=" + params[1] + "&userPassword=" + params[2] + "&userName=" + params[3] + "&userGender=" + params[4];
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
}
