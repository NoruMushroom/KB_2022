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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private Button Comment_Send;
    private EditText Comment_Content;
    private ImageButton Like_Btn;
    private ImageButton Bad_Btn;
    private ImageButton Delete_Btn;
    private String mJsonString;
    private Integer postValue;
    private CommentList_Adapter List_item;
    private LinearLayout Comment_Delete_View;
    private LinearLayout Comment_Send_View;
    private LinearLayout Comment_T;
    private LinearLayout Comment_F;
    private ListView Comment_List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.community_read);
        Intent intent = getIntent();
        Comment_Send = findViewById(R.id.Comment_complete);
        Comment_Content = findViewById(R.id.Comment_content);
        Comment_List = findViewById(R.id.Community_Comment);
        Like_Btn = findViewById(R.id.image_like);
        Bad_Btn = findViewById(R.id.image_unlike);
        Title = findViewById(R.id.R_title);
        Writer = findViewById(R.id.R_writer);
        Content = findViewById(R.id.R_content);
        Like = findViewById(R.id.R_like);
        Delete_Btn = findViewById(R.id.R_delete);
        String number = intent.getStringExtra("글 번호");//string형 글번호 변수
        String userName = intent.getStringExtra("이름");//string형 글번호 변수
        //글 내용 가져오기
        GetData task = new GetData();
        task.execute("readtext", number);
        Comment_Send.setEnabled(false);
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
                Comment_Delete_View = (LinearLayout) View.inflate(Community_Read.this,R.layout.delete_any,null);
                AlertDialog.Builder Dialog = new AlertDialog.Builder(Community_Read.this);
                TextView Text = Comment_Delete_View.findViewById(R.id.Title_Dialog);
                Text.setText("글 삭제");
                Dialog.setView(Comment_Delete_View);
                Dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText PW = Comment_Delete_View.findViewById(R.id.PW_D);
                        String PW_content = PW.getText().toString();
                        GetData task = new GetData();
                        try {
                            String result = task.execute("deletetext", number, PW_content).get();
                            JSONObject j_result = new JSONObject(result);
                            result = j_result.getString("success");//성공 여부
                            Comment_T = (LinearLayout) View.inflate(Community_Read.this,R.layout.success_any,null);
                            Comment_F = (LinearLayout) View.inflate(Community_Read.this,R.layout.failed_any,null);
                            AlertDialog.Builder del_bulider = new AlertDialog.Builder(Community_Read.this);
                            if(result.equals("true")){
                                del_bulider.setView(Comment_T);
                                TextView Text_T = Comment_T.findViewById(R.id.Title_Dialog_T);
                                TextView Text_C = Comment_T.findViewById(R.id.Dialog_T);
                                Text_T.setText("글 삭제 성공");
                                Text_C.setText("글 삭제를 성공하였습니다.");
                                del_bulider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();//인텐트 종료
                                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                                    }
                                });
                            }
                            else{
                                del_bulider.setView(Comment_F);
                                TextView Text_T = Comment_F.findViewById(R.id.Title_Dialog_F);
                                TextView Text_C = Comment_F.findViewById(R.id.Dialog_F);
                                Text_T.setText("글 삭제 실패");
                                Text_C.setText("비밀번호가 맞지 않습니다.");
                                del_bulider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                            }
                            del_bulider.show();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Dialog.show();
            }
        });
        Comment_Content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(Comment_Content.length() > 0){
                    Comment_Send.setEnabled(true);
                }
                else{
                    Comment_Send.setEnabled(false);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Comment_Content.length() > 0){
                    Comment_Send.setEnabled(true);
                }
                else{
                    Comment_Send.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Comment_Content.length() > 0){
                    Comment_Send.setEnabled(true);
                }
                else{
                    Comment_Send.setEnabled(false);
                }
            }
        });
        Comment_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Community_Read.this);
                Comment_Send_View= (LinearLayout) View.inflate(Community_Read.this,R.layout.delete_any,null);
                builder.setView(Comment_Send_View);
                TextView Title = Comment_Send_View.findViewById(R.id.Title_Dialog);
                Title.setText("비밀번호");
                builder.setPositiveButton("댓글 등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText PW = Comment_Send_View.findViewById(R.id.PW_D);
                        String cpw = PW.getText().toString();
                        String comment = Comment_Content.getText().toString();
                        GetData task = new GetData();
                        try {
                            String result = task.execute("writecomment", number, userName ,cpw, comment).get();
                            JSONObject j_result = new JSONObject(result);
                            result = j_result.getString("success");//성공 여부
                            System.out.println(result);
                            Comment_T = (LinearLayout) View.inflate(Community_Read.this,R.layout.success_any,null);
                            Comment_F = (LinearLayout) View.inflate(Community_Read.this,R.layout.failed_any,null);
                            AlertDialog.Builder del_bulider = new AlertDialog.Builder(Community_Read.this);
                            if(result.equals("true")){
                                del_bulider.setView(Comment_T);
                                TextView Text_T = Comment_T.findViewById(R.id.Title_Dialog_T);
                                TextView Text_C = Comment_T.findViewById(R.id.Dialog_T);
                                Text_T.setText("댓글 등록 성공");
                                Text_C.setText("댓글 등록을 성공하였습니다.");
                                del_bulider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();//인텐트 종료
                                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                                        Intent intent = getIntent(); //인텐트
                                        startActivity(intent); //액티비티 열기
                                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                                    }
                                });
                            }
                            else{
                                del_bulider.setView(Comment_F);
                                TextView Text_T = Comment_F.findViewById(R.id.Title_Dialog_F);
                                TextView Text_C = Comment_F.findViewById(R.id.Dialog_F);
                                Text_T.setText("댓글 등록 실패");
                                Text_C.setText("댓글 등록에 실패하였습니다.");
                                del_bulider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                            }
                            del_bulider.show();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        Comment_List.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
                final Comment_Type item = List_item.getItem(a_position);
                String Cno = item.getCno();
                Comment_Delete_View = (LinearLayout) View.inflate(Community_Read.this,R.layout.delete_any,null);
                AlertDialog.Builder Dialog = new AlertDialog.Builder(Community_Read.this);
                TextView text = Comment_Delete_View.findViewById(R.id.Title_Dialog);
                text.setText("댓글 삭제");
                Dialog.setView(Comment_Delete_View);
                Dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText PW = Comment_Delete_View.findViewById(R.id.PW_D);
                        String PW_content = PW.getText().toString();
                        GetData task = new GetData();
                        try {
                            String result = task.execute("deletecomment", Cno, PW_content).get();
                            System.out.println("비밀번호"+PW_content);
                            JSONObject j_result = new JSONObject(result);
                            result = j_result.getString("success");//성공 여부
                            Comment_T = (LinearLayout) View.inflate(Community_Read.this,R.layout.success_any,null);
                            Comment_F = (LinearLayout) View.inflate(Community_Read.this,R.layout.failed_any,null);
                            AlertDialog.Builder del_bulider = new AlertDialog.Builder(Community_Read.this);
                            if(result.equals("true")){
                                del_bulider.setView(Comment_T);
                                TextView Text = Comment_T.findViewById(R.id.Title_Dialog_T);
                                Text.setText("댓글 삭제 성공");
                                del_bulider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();//인텐트 종료
                                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                                        Intent intent = getIntent(); //인텐트
                                        startActivity(intent); //액티비티 열기
                                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                                    }
                                });
                            }
                            else{
                                del_bulider.setView(Comment_F);
                                TextView Text = Comment_F.findViewById(R.id.Title_Dialog_F);
                                Text.setText("댓글 삭제 실패");
                                del_bulider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                            }
                            del_bulider.show();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Dialog.show();
                return true;
            }
        });
    }
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            //setDynamicHeight(listView);
            return true;

        } else {
            return false;
        }

    }
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Community_Read.this,
                    "잠시만 기다려주세요", null, true, true);
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
                else {
                    showDelete();
                }
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://123.215.162.92/KBServer/" + params[0] + ".php";
            String postParameters;
            String Param1;
            String Param2;
            String Param3;
            String Param4;
            switch (params[0]){
                case "readtext":
                    Param1 = params[1];
                    postParameters = "bno=" + Param1;
                    postValue = 1;
                    break;
                case "likeupdown":
                    Param1 = params[1];
                    Param2 = params[2];
                    postParameters = "bno=" + Param1 + "&islike=" + Param2;
                    postValue = 2;
                    break;
                case "deletetext":
                    Param1 = params[1];
                    Param2 = params[2];
                    postParameters = "bno=" + Param1 + "&bpw=" + Param2;
                    postValue = 3;
                    break;
                case "writecomment":
                    Param1 = params[1];
                    Param2 = params[2];
                    Param3 = params[3];
                    Param4 = params[4];
                    postParameters = "bno=" + Param1 + "&uname=" + Param2 + "&cpw=" + Param3 + "&comment=" + Param4;
                    postValue = 3;
                    break;
                case "deletecomment":
                    Param1 = params[1];
                    Param2 = params[2];
                    postParameters = "cno=" + Param1 + "&cpw=" + Param2;
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
        String TAG_JCON = "select_content";
        String TAG_JCOM = "comment_list";
        String TAG_TITLE = "title";
        String TAG_NAME = "name";
        String TAG_CONTENT = "content";
        String TAG_ISLIKE = "islike";
        String TAG_CNO = "cno";
        String TAG_COM = "comment";
        List_item = new CommentList_Adapter();
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JCON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String title = item.getString(TAG_TITLE);
                String name = item.getString(TAG_NAME);
                String content = item.getString(TAG_CONTENT);
                String like = item.getString(TAG_ISLIKE);
                Title.setText(title);
                Writer.setText("작성자 : "+name);
                Content.setText(content);
                Like.setText("좋아요 : "+ like);
                }
            JSONArray jsonArray1 = jsonObject.getJSONArray(TAG_JCOM);
            for(int i = 0; i < jsonArray1.length(); i++){
                JSONObject item = jsonArray1.getJSONObject(i);
                String cno = item.getString(TAG_CNO);
                String name = item.getString(TAG_NAME);
                String comment = item.getString(TAG_COM);
                System.out.println("댓번호 : "+cno+" 작성자 : " + name+" 내용 : "+comment);
                List_item.addItem(comment,name,cno);
                }
            Comment_List.setAdapter(List_item);
            setListViewHeightBasedOnItems(Comment_List);
            }
        catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
