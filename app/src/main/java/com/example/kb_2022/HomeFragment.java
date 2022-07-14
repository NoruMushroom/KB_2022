package com.example.kb_2022;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ListView Trash_List;
    private ImageButton Refresh;
    private ArrayList<BarEntry> Daily_chart = new ArrayList<>(); //일간데이터를 담는곳
    private ArrayList<BarEntry> Weekly_chart = new ArrayList<>();//주간데이터를 담는곳
    private ArrayList<BarEntry> Monthly_chart = new ArrayList<>();//월간데이터를 담는곳
    private ArrayList[] Chart_List = {Daily_chart,Weekly_chart,Monthly_chart};
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
        Trash_List = Home_View.findViewById(R.id.Main_ListView);
        Refresh = Home_View.findViewById(R.id.Refresh);
        Trash_List.setVerticalScrollBarEnabled(false);
        Daily_chart.add(new BarEntry(1,1));
        Daily_chart.add(new BarEntry(2, 2));
        Daily_chart.add(new BarEntry(3, 7));
        Weekly_chart.add(new BarEntry(1, 3));
        Weekly_chart.add(new BarEntry(2, 4));
        Weekly_chart.add(new BarEntry(3, 1));
        Monthly_chart.add(new BarEntry(1, 2));
        Monthly_chart.add(new BarEntry(2, 8));
        Monthly_chart.add(new BarEntry(3, 4));
        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
                Toast.makeText(container.getContext(),"갱신",Toast.LENGTH_SHORT).show();
            }
        });
        dataSetting();
        return Home_View;
    }
    private void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
    private void dataSetting(){
        HomeList_Adapter List_item = new HomeList_Adapter();
        String[] array = new String[]{"일간", "주간", "월간"};
        for (int i=0; i<3; i++) {
            List_item.addItem(array[i],Chart_List[i]);//수치랑 이름 같이 넘겨주기
        }//일간 주간 월간 리스트뷰 아이템 생성
        /* 리스트뷰에 어댑터 등록 */
        Trash_List.setAdapter(List_item);
    }
}