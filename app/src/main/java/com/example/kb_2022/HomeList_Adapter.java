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

import java.util.ArrayList;

public class HomeList_Adapter extends BaseAdapter {
    private ArrayList<HomeList_Type> mItems = new ArrayList<>();
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
        configureChartAppearance(Trash_Bar);
        Trash_Bar.invalidate(); // 차트 업데이트
        Trash_Bar.setTouchEnabled(false);
        return convertView;
    }
    private void configureChartAppearance(BarChart Trash_Bar) {
        Trash_Bar.setDrawBarShadow(false);//그림자 효과
        Trash_Bar.getDescription().setEnabled(false); // chart 밑에 description 표시 유무
        Trash_Bar.setMaxVisibleValueCount(3);
        Trash_Bar.setTouchEnabled(false); // 터치 유무
        Trash_Bar.getLegend().setEnabled(false); // Legend는 차트의 범례

        // XAxis (수평 막대 기준 왼쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        XAxis xAxis = Trash_Bar.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(13f);
        xAxis.setGridLineWidth(25f);
        xAxis.setGridColor(Color.parseColor("#80E5E5E5"));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X 축 데이터 표시 위치

        // YAxis(Left) (수평 막대 기준 아래쪽) - 선 유무, 데이터 최솟값/최댓값, label 유무
        YAxis axisLeft = Trash_Bar.getAxisLeft();
        axisLeft.setDrawGridLines(true);
        axisLeft.setDrawAxisLine(true);
        axisLeft.setAxisMinimum(0f); // 최솟값
        axisLeft.setAxisMaximum(10f); // 최댓값
        axisLeft.setGranularity(2f); // 값만큼 라인선 설정
        axisLeft.setDrawLabels(true); // label 삭제


        // YAxis(Right) (수평 막대 기준 위쪽) - 사이즈, 선 유무
        YAxis axisRight = Trash_Bar.getAxisRight();
        axisRight.setTextSize(15f);
        axisRight.setDrawLabels(false); // label 삭제
        axisRight.setDrawGridLines(false);
        axisRight.setDrawAxisLine(false);
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