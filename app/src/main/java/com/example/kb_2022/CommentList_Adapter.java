package com.example.kb_2022;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CommentList_Adapter extends BaseAdapter {
    private ArrayList<Comment_Type> mItems = new ArrayList<>();
    public CommentList_Adapter(){

    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Comment_Type getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        Context context = parent.getContext();//

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.community_comment_item, parent, false);
        }
        TextView comment_username = convertView.findViewById(R.id.comment_username);
        TextView comment_content = convertView.findViewById(R.id.comment_content);
        ImageView comment_photo = convertView.findViewById(R.id.comment_photo);
        GradientDrawable drawable = (GradientDrawable)context.getDrawable(R.drawable.round_image);
        comment_photo.setBackground(drawable);
        comment_photo.setClipToOutline(true);
        Comment_Type myItem = getItem(position);
        comment_content.setText(myItem.getContent());
        comment_username.setText("작성자 : " + myItem.getWriter());
        return convertView;
    }

    public void addItem(String contents, String Writer,String Cno) {//매개변수 바꿔야됨
        //Drawable img, String name, String contents
        /* MyItem에 아이템을 setting한다. */
        //mItem.setIcon(img);
        Comment_Type mItem = new Comment_Type();

        mItem.setWriter(Writer);
        mItem.setContent(contents);
        mItem.setCno(Cno);
        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);//
    }
}