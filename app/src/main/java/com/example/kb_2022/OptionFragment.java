package com.example.kb_2022;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button Change_Name;
    private Button Sign_out;
    private LinearLayout PW_View;
    private LinearLayout Name_View;
    private LinearLayout Member_View;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Option_View = inflater.inflate(R.layout.fragment_option, container, false);
        Change_Photo = Option_View.findViewById(R.id.option_change_photo);
        Change_PW = Option_View.findViewById(R.id.option_change_pw);
        Change_Name = Option_View.findViewById(R.id.option_change_name);
        Sign_out = Option_View.findViewById(R.id.option_sign_out);
        Change_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        Before_PW = PW_View.findViewById(R.id.input_before_PW);
                        After_PW = PW_View.findViewById(R.id.input_after_PW);

                    }
                });
                Dialog.setNegativeButton("취소", null);
                Dialog.show();
            }
        });
        Change_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name_View = (LinearLayout) View.inflate(getActivity(),R.layout.change_name,null);
                AlertDialog.Builder Dialog = new AlertDialog.Builder(getActivity());
                Dialog.setTitle("이름 변경");
                Dialog.setView(Name_View);
                Dialog.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Name_PW = Name_View.findViewById(R.id.Name_PW);
                        After_Name = Name_View.findViewById(R.id.input_after_Name);
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
                    }
                });
                Dialog.setNegativeButton("취소", null);
                Dialog.show();
            }
        });
        return Option_View;
    }
}