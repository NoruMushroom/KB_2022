package com.example.kb_2022;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ListView Trash_List;
    private ImageButton Refresh;
    private Context This_Activity;
    private String userID;
    private String month;
    private ArrayList<Integer> chart_Data = new ArrayList<>();
    private ArrayList<BarEntry> Daily_chart = new ArrayList<>(); //일간데이터를 담는곳
    private ArrayList<BarEntry> Weekly_chart = new ArrayList<>();//주간데이터를 담는곳
    private ArrayList<BarEntry> Monthly_chart = new ArrayList<>();//월간데이터를 담는곳
    private ArrayList[] Chart_List = {Daily_chart,Weekly_chart,Monthly_chart};
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  JSONObject item;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Home_View = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            bundle = getArguments();
            userID = bundle.getString("아이디");
        }
        Toast.makeText(container.getContext(), userID,Toast.LENGTH_SHORT).show();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        month = monthFormat.format(currentTime);//현재 달
        Trash_List = Home_View.findViewById(R.id.Main_ListView);
        This_Activity = container.getContext();
        Refresh = Home_View.findViewById(R.id.Refresh);
        Trash_List.setVerticalScrollBarEnabled(false);
        month = month.replaceAll("^0+","");
        Toast.makeText(container.getContext(), month,Toast.LENGTH_SHORT).show();
        Daily_chart.add(new BarEntry(1,100));
        Daily_chart.add(new BarEntry(2, 200));
        Daily_chart.add(new BarEntry(3, 300));
        Daily_chart.add(new BarEntry(4,400));
        Daily_chart.add(new BarEntry(5, 500));
        Daily_chart.add(new BarEntry(6, 600));
        Daily_chart.add(new BarEntry(7, 700));
        Weekly_chart.add(new BarEntry(1, 500));
        Weekly_chart.add(new BarEntry(2, 600));
        Weekly_chart.add(new BarEntry(3, 700));
        Weekly_chart.add(new BarEntry(4, 800));
        Monthly_chart.add(new BarEntry(1, 700));
        Monthly_chart.add(new BarEntry(2, 600));
        Monthly_chart.add(new BarEntry(3, 500));
        GetData task = new GetData();
        task.execute(userID, month);
        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
                Toast.makeText(container.getContext(),"갱신",Toast.LENGTH_SHORT).show();
            }
        });
        try {
            dataSetting();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Home_View;
    }
    private void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
    private void dataSetting() throws JSONException {
//g0801이런식으로
        int n_month = Integer.parseInt(month);
        JSONObject jsonmonth1 = item.getJSONObject(month);//이번달
        JSONObject jsonmonth2 = item.getJSONObject(Integer.toString(n_month - 1));//저번달
        JSONObject jsonmonth3 = item.getJSONObject(Integer.toString(n_month - 2));//저저번달
        Toast.makeText(This_Activity, jsonmonth1.length(),Toast.LENGTH_SHORT).show();
        Toast.makeText(This_Activity, jsonmonth2.length(),Toast.LENGTH_SHORT).show();
        Toast.makeText(This_Activity, jsonmonth3.length(),Toast.LENGTH_SHORT).show();

        ArrayList<BarEntry> Monthly_chart = new ArrayList<>();//월간데이터를 담는곳
        HomeList_Adapter List_item = new HomeList_Adapter();
        String[] array = new String[]{"일간", "주간", "월간"};
        for (int i=0; i<3; i++) {
            List_item.addItem(array[i],Chart_List[i]);//수치랑 이름 같이 넘겨주기
            //chart_List에 값을 바꾸면 실시간
        }//일간 주간 월간 리스트뷰 아이템 생성
        /* 리스트뷰에 어댑터 등록 */
        Trash_List.setAdapter(List_item);
    }
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(This_Activity,
                    "잠시만 기다려주세요", null, true, true);
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
                try {
                    item = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://123.215.162.92/KBServer/homelist.php";
            String postParameters = "userID=" + params[0] + "&mon=" + params[1];
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
}