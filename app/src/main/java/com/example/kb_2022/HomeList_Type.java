package com.example.kb_2022;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeList_Type {

        private Drawable icon;
        private String name;
        private String contents;
        private ArrayList<BarEntry> Chart_List;

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public void setBar_Data(ArrayList<BarEntry> Chart_Data){ this.Chart_List = Chart_Data; }
        public BarData getBar_Data(){
            BarData barData = new BarData();
            BarDataSet barDataSet = new BarDataSet(Chart_List, "bardataset");
            barDataSet.setColor(Color.WHITE);
            barData.setBarWidth(0.3f);
            barData.addDataSet(barDataSet);
            return barData;
        }

        public String getContents() {
            return contents;
        }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
