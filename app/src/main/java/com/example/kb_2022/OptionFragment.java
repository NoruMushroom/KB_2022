package com.example.kb_2022;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

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
    private TextView Change_Photo;
    private TextView Change_PW;
    private TextView Logout;
    private TextView Sign_out;
    private String userID;
    private ImageView User_image;
    private LinearLayout PW_View;
    private LinearLayout Logout_View;
    private LinearLayout Member_View;
    private EditText Before_PW;
    private EditText After_PW;
    private EditText ID;
    private EditText PW;
    public static final int REQUEST_CODE = 100;

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            Bitmap bitmap = null;
            Bitmap Rotate_bitmap = null;
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                Rotate_bitmap = bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                User_image.setImageBitmap(Rotate_bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File f = BitToFile(Rotate_bitmap, userID);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), f);
            MultipartBody.Part body = MultipartBody.Part.createFormData("myfile", f.getName(), requestBody);
            AndClient.sguploadInterface sguploadInterface = AndClient.requestServer.getClient().create(AndClient.sguploadInterface.class);
            Call<AndClient.sguploadResponse> call = sguploadInterface.sgUpload(body);
            call.enqueue(new Callback<AndClient.sguploadResponse>() {
                @Override
                public void onResponse(Call<AndClient.sguploadResponse> call, Response<AndClient.sguploadResponse> response) {
                    System.out.println("성공 여부 : " + response.body().sguploadResponse());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("이미지 변경 성공");
                    builder.setMessage("\n이미지 변경을 완료하였습니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                }

                @Override
                public void onFailure(Call<AndClient.sguploadResponse> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            bundle = getArguments();
            userID = bundle.getString("아이디");
        }
        View Option_View = inflater.inflate(R.layout.fragment_option, container, false);
        Change_Photo = Option_View.findViewById(R.id.option_change_photo);
        Change_PW = Option_View.findViewById(R.id.option_change_pw);
        Logout = Option_View.findViewById(R.id.option_logout);
        Sign_out = Option_View.findViewById(R.id.option_sign_out);
        User_image = Option_View.findViewById(R.id.User_photo_option);
        GradientDrawable drawable = (GradientDrawable)getContext().getDrawable(R.drawable.round_image);
        User_image.setBackground(drawable);
        User_image.setClipToOutline(true);
        new Thread(() -> {
            userImgInterface userimgIF = requestServer.getClient().create(OptionFragment.userImgInterface.class);
            Call<ResponseBody> call = userimgIF.userImgPost(userID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    User_image.setImageBitmap(bitmap);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }).start();
        Change_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQUEST_CODE);
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
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout_View = (LinearLayout) View.inflate(getActivity(),R.layout.logout,null);
                AlertDialog.Builder Dialog = new AlertDialog.Builder(getActivity());
                Dialog.setTitle("로그아웃");
                Dialog.setView(Logout_View);
                Dialog.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });
                Dialog.setNegativeButton("취소", null);
                Dialog.show();
            }
        });
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
    private File BitToFile(Bitmap bitmap, String user){
        File file = new File(getActivity().getCacheDir(), user+".jpeg");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }
    private static class requestServer{
        private static final String Server_URL = "http://123.215.162.92/";
        private static Retrofit retrofit;

        private static Retrofit getClient(){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            if(retrofit == null){
                retrofit = new Retrofit.Builder()
                        .baseUrl(Server_URL)
                        .addConverterFactory(new AndClient.NullOnEmptyConverterFactory())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
            return retrofit;
        }
    }
    private interface userImgInterface{
        @GET("android/user_img/{userid}/{userid}.jpeg")
        @Streaming
        Call<ResponseBody> userImgPost(
                @Path("userid") String userid);
    }

}