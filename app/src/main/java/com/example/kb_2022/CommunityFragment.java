package com.example.kb_2022;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ListView Community_List;
    private Context This_Activity;
    private static String IP_ADDRESS = "http://123.215.162.92/KBServer/readboard.php";
    private static String TAG = "phptest";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mJsonString;
    private CommunityList_Adapter List_item;
    private ArrayAdapter adapter;
    private String userName;
    private String userGender;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunityFragment newInstance(String param1, String param2) {
        CommunityFragment fragment = new CommunityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        dataSetting();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        This_Activity = container.getContext();
        Bundle bundle = this.getArguments();
        if(bundle != null){
            bundle = getArguments();
            userName = bundle.getString("이름");
            userGender = bundle.getString("성별");
        }
        View Community_View = inflater.inflate(R.layout.fragment_community, container, false);
        System.out.println("아이디" + This_Activity);
        Community_List = Community_View.findViewById(R.id.Community_ListView);//리스트뷰
        ImageButton Write_icon = Community_View.findViewById(R.id.Write);
        Write_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"이름 : " + userName + " 성별 : " + userGender, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Community_Write.class);
                intent.putExtra("이름", userName);
                startActivity(intent);
            }//글쓰기화면 이동
        });
        dataSetting();
        Community_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {
                final Community_Type item = (Community_Type) List_item.getItem(a_position);
                String Number = item.getNumber();
                Intent intent = new Intent(getActivity(), Community_Read.class);
                intent.putExtra("글 번호",Number);//값 전달해주기
                startActivity(intent);
            }
        });
        return Community_View;
    }
    private void dataSetting(){
        GetData task = new GetData();
        task.execute(IP_ADDRESS, "");
    }
    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(This_Activity,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result == null){
                Log.d(TAG, "response - " + result);
                //실패시
            }
            else {
                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String postParameters = params[1];
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
                Log.d(TAG, "response code - " + responseStatusCode);
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
        String TAG_JSON = "board_list";
        String TAG_BNO = "bno";
        String TAG_TITLE = "title";
        String TAG_UNAME = "name";
        String TAG_LIKE = "islike";
        List_item = new CommunityList_Adapter();
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                Integer bno = item.getInt(TAG_BNO);
                String title = item.getString(TAG_TITLE);
                String uname = item.getString(TAG_UNAME);
                Integer like = item.getInt(TAG_LIKE);
                List_item.addItem(title,uname,like,bno);
            }
            Community_List.setAdapter(List_item);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}