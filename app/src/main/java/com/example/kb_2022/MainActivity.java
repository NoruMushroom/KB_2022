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
    String userName;
    String userGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        userName = intent.getStringExtra("이름");
        userGender = intent.getStringExtra("성별");
        Home = new HomeFragment();
        Calender = new CalendarFragment();
        Community = new CommunityFragment();
        Option = new OptionFragment();//객체 생성
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,Home).commitAllowingStateLoss();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,Home).commitAllowingStateLoss();
                        return true;
                    case R.id.calendar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,Calender).commitAllowingStateLoss();
                        return true;
                    case R.id.community:
                        Bundle bundle = new Bundle();
                        bundle.putString("이름",userName);
                        bundle.putString("성별",userGender);
                        Community.setArguments(bundle);
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