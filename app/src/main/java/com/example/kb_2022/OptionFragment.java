package com.example.kb_2022;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button Change_Photo;
    private Button Change_PW;
    private Button Logout;
    private Button Sign_out;
    private String userID;
    private ImageView User_image;
    private LinearLayout PW_View;
    private LinearLayout Photo_View;
    private LinearLayout Logout_View;
    private LinearLayout Member_View;
    private ArrayList<Photo_Type> mList;
    private EditText Before_PW;
    private EditText After_PW;
    private EditText Name_PW;
    private EditText After_Name;
    private EditText ID;
    private EditText PW;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OptionFragment newInstance(String param1, String param2) {
        OptionFragment fragment = new OptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void addItem(int img_path,String imgName){
        Photo_Type item = new Photo_Type();
        item.set_Photo_Path(img_path);
        item.set_Photo_Name(imgName);
        mList.add(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if(bundle != null){
            bundle = getArguments();
            userID = bundle.getString("아이디");
        }
        View Option_View = inflater.inflate(R.layout.fragment_option, container, false);
        Change_Photo = Option_View.findViewById(R.id.option_change_photo);
        Change_PW = Option_View.findViewById(R.id.option_change_pw);
        Logout = Option_View.findViewById(R.id.option_change_name);
        Sign_out = Option_View.findViewById(R.id.option_sign_out);
        User_image = Option_View.findViewById(R.id.User_photo_option);
        mList = new ArrayList<>();
        addItem(R.drawable.chu,"츄");
        addItem(R.drawable.lee,"아이유");
        addItem(R.drawable.han,"한효주");
        addItem(R.drawable.suzi,"수지");
        Change_Photo.setOnClickListener(new View.OnClickListener() {
            int Picture_id = -1;
            @Override
            public void onClick(View v) {
                Photo_View = (LinearLayout) View.inflate(getActivity(),R.layout.change_photo,null);
                AlertDialog.Builder Dialog = new AlertDialog.Builder(getActivity());
                Dialog.setTitle("프로필 사진 변경");
                Dialog.setView(Photo_View);
                RecyclerView recyclerView = Photo_View.findViewById(R.id.photo_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),recyclerView.HORIZONTAL,false));
                Photo_Adapter adapter = new Photo_Adapter(mList);
                adapter.setOnItemClickListener(new Photo_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int pos) {
                        Picture_id = mList.get(pos).get_Photo_Path();
                    }//사진의 주소를 받아온다.
                });
                recyclerView.setAdapter(adapter);
                Dialog.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(),String.valueOf(Picture_id),Toast.LENGTH_SHORT).show();
                            if(Picture_id >= 0) {
                                User_image.setImageResource(Picture_id);
                            }
                    }//출력하기
                });
                Dialog.setNegativeButton("취소", null);
                Dialog.show();
            }
        });
        Change_PW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PW_View = (LinearLayout) View.inflate(getActivity(),R.layout.change_pw,null);
                AlertDialog.Builder Dialog = new AlertDialog.Builder(getActivity());
                Dialog.setTitle("비밀번호 변경");
                Dialog.setView(PW_View);
                Dialog.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //비밀번호 변경
                        Before_PW = PW_View.findViewById(R.id.input_before_PW);
                        After_PW = PW_View.findViewById(R.id.input_after_PW);
                        AndClient.pwchangeInterface pwchangeInterface = AndClient.requestServer.getClient().create(AndClient.pwchangeInterface.class);
                        Call<AndClient.pwchangeResponse> pwcall = pwchangeInterface.pwchangePost(userID, Before_PW.getText().toString(), After_PW.getText().toString());
                        pwcall.enqueue(new Callback<AndClient.pwchangeResponse>() {
                            @Override
                            public void onResponse(Call<AndClient.pwchangeResponse> call, Response<AndClient.pwchangeResponse> response) {
                                System.out.println(response.body().pwRespoense());
                            }

                            @Override
                            public void onFailure(Call<AndClient.pwchangeResponse> call, Throwable t) {

                            }
                        });
                    }
                });
                Dialog.setNegativeButton("취소", null);
                Dialog.show();
            }
        });
        /*Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout_View = (LinearLayout) View.inflate(getActivity(),R.layout.,null);
                AlertDialog.Builder Dialog = new AlertDialog.Builder(getActivity());
                Dialog.setTitle("로그아웃");
                Dialog.setView(Logout_View);
                Dialog.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                Dialog.setNegativeButton("취소", null);
                Dialog.show();
            }
        });*/
        Sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Member_View = (LinearLayout) View.inflate(getActivity(),R.layout.sign_out,null);
                AlertDialog.Builder Dialog = new AlertDialog.Builder(getActivity());
                Dialog.setTitle("회원 탈퇴");
                Dialog.setView(Member_View);
                Dialog.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ID = Member_View.findViewById(R.id.input_Out_ID);
                        PW = Member_View.findViewById(R.id.input_Out_PW);
                        AndClient.signoutInterface signoutInterface = AndClient.requestServer.getClient().create(AndClient.signoutInterface.class);
                        Call<AndClient.signoutResponse> signoutcall = signoutInterface.singoutPost(ID.getText().toString(), PW.getText().toString());
                        signoutcall.enqueue(new Callback<AndClient.signoutResponse>() {
                            @Override
                            public void onResponse(Call<AndClient.signoutResponse> call, Response<AndClient.signoutResponse> response) {
                                System.out.println(response.body().signoutResponse());
                            }

                            @Override
                            public void onFailure(Call<AndClient.signoutResponse> call, Throwable t) {

                            }
                        });
                    }
                });
                Dialog.setNegativeButton("취소", null);
                Dialog.show();
            }
        });
        return Option_View;
    }
}