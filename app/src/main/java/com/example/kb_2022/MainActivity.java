package com.example.kb_2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment Home;
    Fragment Calender;
    Fragment Community;
    Fragment Option;//페이지 변수 선언
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Home = new HomeFragment();
        Calender = new CalendarFragment();
        Community = new CommunityFragment();
        Option = new OptionFragment();//객체 생성
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,Home).commitAllowingStateLoss();
        //bottomNavigationView = findViewById(R.id.navi);
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
}