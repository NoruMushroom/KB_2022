package com.example.kb_2022;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Photo_Adapter extends RecyclerView.Adapter<Photo_Adapter.ViewHolder> {
    private ArrayList<Photo_Type> mList = null;
    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public interface OnLongItemClickListener {
        void onLongItemClick(int pos);
    }

    private OnLongItemClickListener onLongItemClickListener = null;

    public void setOnLongItemClickListener(OnLongItemClickListener listener) {
        this.onLongItemClickListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        TextView img_name;
        public ViewHolder(View itemView) {
            super(itemView);
            img_item = (ImageView) itemView.findViewById(R.id.show_photo);
            img_name = (TextView) itemView.findViewById(R.id.photo_name);
            GradientDrawable drawable = (GradientDrawable)itemView.getContext().getDrawable(R.drawable.round_image);
            img_item.setBackground(drawable);
            img_item.setClipToOutline(true);
            img_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
    public Photo_Adapter(ArrayList<Photo_Type> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.change_photo_item, parent, false);
        Photo_Adapter.ViewHolder vh = new Photo_Adapter.ViewHolder(view);
        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(Photo_Adapter.ViewHolder holder, int position) {
        Photo_Type item = mList.get(position);
        holder.img_item.setImageResource(item.get_Photo_Path());   // 사진 없어서 기본 파일로 이미지 띄움
        holder.img_name.setText(item.get_Photo_Name());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
