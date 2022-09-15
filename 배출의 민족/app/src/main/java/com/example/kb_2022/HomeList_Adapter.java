package com.example.kb_2022;

import android.content.Context;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeList_Adapter extends BaseAdapter {
    private ArrayList<HomeList_Type> mItems = new ArrayList<>();
    private int option = 0;
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public HomeList_Type getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();//

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.home_listview_item, parent, false);
        }
        TextView Text = convertView.findViewById(R.id.Trash_option);
        BarChart Trash_Bar = convertView.findViewById(R.id.chart);
        HomeList_Type myItem = getItem(position);
        Text.setText(myItem.getName());
        Trash_Bar.setData(myItem.getBar_Data());
        if(myItem.getName().equals("일간")){
            option = 7;
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            String[] days = {" "," "," "," "," "," "," "," "};
            days[7] = format.format(currentTime);
            Calendar calendar = Calendar.getInstance();
            for(int i = 0; i < 6; i++){
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                //Date date = calendar.getTime();
                days[6 - i] = format.format(calendar.getTime());
            }
            configureChartAppearance(Trash_Bar,days);
        }
        if(myItem.getName().equals("주간")){
            option = 4;
            String[] week = {" ","1주차", "2주차", "3주차", "4주차"};
            configureChartAppearance(Trash_Bar,week);
        }
        if(myItem.getName().equals("월간")){
            String[] Month = {" "," "," "," "};
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
            String month = monthFormat.format(currentTime);//현재 달
            month = month.replaceAll("^0+","");
            int I_Month = Integer.parseInt(month);
            for(int i = 0; i < 3; i++){
                Month[3 - i] = Integer.toString(I_Month - i) + "월";
            }
            option = 3;
            configureChartAppearance(Trash_Bar,Month);
        }
        Trash_Bar.setTouchEnabled(false);
        Trash_Bar.animateXY(1000, 1000);
        Trash_Bar.invalidate();// 차트 업데이트
        return convertView;
    }
    private void configureChartAppearance(BarChart Trash_Bar, String[] option_array) {
        Trash_Bar.getLegend().setEnabled(false); // Legend는 차트의 범례
        Trash_Bar.setDrawGridBackground(false);//격자 출력 유무
        Trash_Bar.setDrawBarShadow(false);//그림자 효과
        Trash_Bar.getDescription().setEnabled(false);
        // XAxis (수평 막대 기준 왼쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        XAxis xAxis = Trash_Bar.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X 축 데이터 표시 위치
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);//선출력
        xAxis.setGranularity(1f);
        xAxis.setGridLineWidth(20f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(option_array));
        YAxis yAxis = Trash_Bar.getAxisLeft();
        Trash_Bar.getAxisRight().setEnabled(false);
        xAxis.setTextSize(10f);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(true);//선출력
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(5000f);
    }
    public void addItem(String name, ArrayList<BarEntry> Chart_List) {//Bar = 차트 위젯, chart = 차트 데이터
        //Drawable img, String name, String contents
        /* MyItem에 아이템을 setting한다. */
        //mItem.setIcon(img);
        HomeList_Type mItem = new HomeList_Type();
        mItem.setName(name);
        mItem.setBar_Data(Chart_List);
        //mItem.setContents(contents);
        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);
    }
}