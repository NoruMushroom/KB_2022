package com.example.kb_2022;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Home_View = inflater.inflate(R.layout.fragment_home, container, false);
        TextView text = Home_View.findViewById(R.id.Home_text);
        text.setText("여기는 홈 화면 입니다.");
        // Inflate the layout for this fragment
        return Home_View;
    }
}