package com.example.kb_2022;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 카메라 촬영을 하면 이미지뷰에 사진 삽입
        if (requestCode == 0 && resultCode == RESULT_OK) {
            // Bundle로 데이터를 입력
            Bundle extras = data.getExtras();

            // Bitmap으로 컨버전
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // 이미지뷰에 Bitmap으로 이미지를 입력
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            image.setImageBitmap(imageBitmap);
        }
    }*/
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Photo_View = inflater.inflate(R.layout.fragment_photo, container, false);
        Take_Photo = Photo_View.findViewById(R.id.Take);
        Analyze_Photo = Photo_View.findViewById(R.id.Analyze);
        capture_image = Photo_View.findViewById(R.id.capture_Image);
        surfaceView = Photo_View.findViewById(R.id.surface_Image);
        result = Photo_View.findViewById(R.id.Result);
        result.setText("쓰레기 사진을 찍어주세요");
        Analyze_Photo.setEnabled(false);
        Take_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture();
            }
        });
        Analyze_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("사진을 분석중입니다");
            }
        });
        return Photo_View;
    }
    public void capture() {
        surfaceView.capture(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //bytearray 형식으로 전달
                //이걸이용해서 이미지뷰로 보여주거나 파일로 저장
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8; // 1/8사이즈로 보여주기
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options); //data 어레이 안에 있는 데이터 불러와서 비트맵에 저장
                Rotate_Bitmap = bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);//전역변수해야됨
                capture_image.setImageBitmap(Rotate_Bitmap);//이미지뷰에 사진 보여주기
                Analyze_Photo.setEnabled(true);
                result.setText("사진 분석 버튼을 눌러주세요");
                Take_Photo.setText("사진 재촬영");
                camera.startPreview();
            }
        });
    }
}