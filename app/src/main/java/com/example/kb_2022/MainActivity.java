package com.example.kb_2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Fragment Home;
    Fragment Calender;
    Fragment Community;
    Fragment Option;//페이지 변수 선언
    private String userName;
    private String userGender;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        userName = intent.getStringExtra("이름");
        userGender = intent.getStringExtra("성별");
        userID = intent.getStringExtra("아이디");
        Home = new HomeFragment();
        Calender = new CalendarFragment();
        Community = new CommunityFragment();
        Option = new OptionFragment();//객체 생성
        Bundle home_bundle = new Bundle();
        home_bundle.putString("아이디",userID);
        Home.setArguments(home_bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,Home).commitAllowingStateLoss();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Bundle home_bundle = new Bundle();
                        home_bundle.putString("아이디",userID);
                        Home.setArguments(home_bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,Home).commitAllowingStateLoss();
                        return true;
                    case R.id.calendar:
                        Bundle Cal_bundle = new Bundle();
                        Cal_bundle.putString("아이디",userID);
                        Calender.setArguments(Cal_bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,Calender).commitAllowingStateLoss();
                        return true;
                    case R.id.community:
                        Bundle Com_bundle = new Bundle();
                        Com_bundle.putString("이름",userName);
                        Com_bundle.putString("성별",userGender);
                        Community.setArguments(Com_bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,Community).commitAllowingStateLoss();
                        return true;
                    case R.id.option:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,Option).commitAllowingStateLoss();
                        return true;
                }
                return true;
            }
        });
    }
}//