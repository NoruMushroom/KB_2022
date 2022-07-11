package com.example.kb_2022;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CommunityList_Adapter extends BaseAdapter {
    private ArrayList<Community_Type> mItems = new ArrayList<>();
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Community_Type getItem(int position) {
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
            convertView = inflater.inflate(R.layout.community_listview_item, parent, false);
        }
        TextView Title = convertView.findViewById(R.id.Title);
        TextView Like = convertView.findViewById(R.id.Like);
        TextView Number = convertView.findViewById(R.id.Number);
        TextView Writer = convertView.findViewById(R.id.Writer);
        Community_Type myItem = getItem(position);
        Title.setText(myItem.getTitle());
        Like.setText(myItem.getLike());
        Number.setText(myItem.getNumber());
        Writer.setText(myItem.getWriter());
        return convertView;
    }

    public void addItem(String Title, String Writer, int Like, int Number) {//매개변수 바꿔야됨
        //Drawable img, String name, String contents
        /* MyItem에 아이템을 setting한다. */
        //mItem.setIcon(img);
        Community_Type mItem = new Community_Type();
        mItem.setTitle(Title);
        mItem.setWriter(Writer);
        mItem.setLike(Like);
        mItem.setNumber(Number);
        //mItem.setContents(contents);
        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);//
    }
}