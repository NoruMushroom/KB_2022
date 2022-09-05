package com.example.kb_2022;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

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
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class PhotoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button Take_Photo;
    private Button Analyze_Photo;
    private ImageView capture_image;
    private TextView result;
    private CameraSurfaceView surfaceView;
    private Bitmap Rotate_Bitmap;
    private boolean Hide = false;
    private Context thiscontext;
    private String userID;

    public PhotoFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if(bundle != null){
            bundle = getArguments();
            userID = bundle.getString("아이디");
        }
        View Photo_View = inflater.inflate(R.layout.fragment_photo, container, false);
        Take_Photo = Photo_View.findViewById(R.id.Take);
        Analyze_Photo = Photo_View.findViewById(R.id.Analyze);
        capture_image = Photo_View.findViewById(R.id.capture_Image);
        surfaceView = Photo_View.findViewById(R.id.surface_Image);
        result = Photo_View.findViewById(R.id.Result);
        result.setText("쓰레기 사진을 찍어주세요");
        Analyze_Photo.setEnabled(false);
        thiscontext = container.getContext();

        Take_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Take_Photo.getText().equals("사진 재촬영")){
                    capture_image.setImageResource(0);
                    Take_Photo.setText("사진 촬영");
                    result.setText("쓰레기 사진을 찍어주세요");
                    Analyze_Photo.setEnabled(false);
                }
                else {
                    capture();
                }
            }
        });
        Analyze_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("사진을 분석중입니다");
                File upfile = BitToUri(Rotate_Bitmap);
                System.out.println("파일 : "+upfile);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), upfile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("myfile", upfile.getName(), requestBody);
                postInterface postInterface = andClient.getClient().create(PhotoFragment.postInterface.class);
                Call<ServerResponse> call = postInterface.imgUpload(body);
                call.enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        System.out.println("응답코드 : "+response.code()+"\n 데이터 : " + response.body());
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        System.out.println("개같이 실패");
                    }
                });
                analyze analyzeIF = andClient.getClient().create(PhotoFragment.analyze.class);
                Call<AnalyzeResponse> call1 = analyzeIF.getResult("123");
                System.out.println("실행");
                call1.enqueue(new Callback<AnalyzeResponse>() {
                    @Override
                    public void onResponse(Call<AnalyzeResponse> call, Response<AnalyzeResponse> response) {
                        result.setText(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<AnalyzeResponse> call, Throwable t) {
                    }
                });
            }
        });
        return Photo_View;
    }
    private void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
    public void capture() {
        surfaceView.capture(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //bytearray 형식으로 전달
                //이걸이용해서 이미지뷰로 보여주거나 파일로 저장
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4; // 1/8사이즈로 보여주기
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options); //data 어레이 안에 있는 데이터 불러와서 비트맵에 저장
                Rotate_Bitmap = bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);//전역변수해야됨
                Analyze_Photo.setEnabled(true);
                result.setText("사진 분석 버튼을 눌러주세요");
                Take_Photo.setText("사진 재촬영");
                capture_image.setImageBitmap(Rotate_Bitmap);//이미지뷰 보이기
                camera.startPreview();
            }
        });
    }
    private interface postInterface{
        @Multipart
        @POST("upload.php")
        Call<ServerResponse> imgUpload(@Part MultipartBody.Part image);

    }
    private interface analyze{
        @FormUrlEncoded
        @POST("hw.py")
        Call<AnalyzeResponse> getResult(@Field("userID") String userid);
    }
    private static class andClient{
        private static final String Server_URL = "http://123.215.162.92/KBServer/";
        private static Retrofit retrofit;

        private static Retrofit getClient(){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            if(retrofit == null){
                retrofit = new Retrofit.Builder()
                        .baseUrl(Server_URL)
                        .addConverterFactory(new NullOnEmptyConverterFactory())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
            return retrofit;
        }
    }
    private static class NullOnEmptyConverterFactory extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit)
        {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException
                {
                    if (body.contentLength() == 0) {
                        return null;
                    }
                    return delegate.convert(body);
                }
            };
        }
    }
    private class ServerResponse{
        @SerializedName("success")
        boolean success;
        @SerializedName("error")
        String error;

        public String toString() {
            return success + error;
        }
    }
    private class AnalyzeResponse{
        @SerializedName("percent")
        String percent;
        @SerializedName("result")
        String result;


        public String toString(){
            if (result == "fail"){
                return "해당 이미지 분석에 실패했습니다. 다시 찍어주세요.";
            }
            else{
                return "해당 쓰레기는 " + result + "일 확률이 " + percent + "% 입니다.";
            }
        }
    }
    private File BitToUri(Bitmap bitmap){
        File file = new File(thiscontext.getCacheDir(), "123.jpeg");
        FileOutputStream out = null;
        try {
             out = new FileOutputStream(file);
             bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

}