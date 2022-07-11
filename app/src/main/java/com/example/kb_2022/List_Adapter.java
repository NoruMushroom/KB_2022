package com.example.kb_2022;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;

public class List_Adapter extends BaseAdapter {
    private ArrayList<List_Type> mItems = new ArrayList<>();
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public List_Type getItem(int position) {
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
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }
        TextView Text = convertView.findViewById(R.id.Trash_option);
        BarChart Trash_Bar = convertView.findViewById(R.id.chart);
        List_Type myItem = getItem(position);
        Text.setText(myItem.getName());
        Trash_Bar.setData(myItem.getBar_Data());
        Trash_Bar.invalidate(); // 차트 업데이트
        Trash_Bar.setTouchEnabled(false);
        return convertView;
    }

    public void addItem(String name, ArrayList<BarEntry> Chart_List) {//Bar = 차트 위젯, chart = 차트 데이터
        //Drawable img, String name, String contents
        /* MyItem에 아이템을 setting한다. */
        //mItem.setIcon(img);
        List_Type mItem = new List_Type();
        mItem.setName(name);
        mItem.setBar_Data(Chart_List);
        //mItem.setContents(contents);
        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);
    }
}