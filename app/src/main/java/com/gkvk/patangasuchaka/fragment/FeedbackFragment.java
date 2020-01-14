package com.gkvk.patangasuchaka.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gkvk.R;
import com.gkvk.patangasuchaka.main.LoginActivity;
import com.gkvk.patangasuchaka.main.SignUpActivity;
import com.gkvk.patangasuchaka.util.ApplicationConstant;


public class FeedbackFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ProgressDialog dialog;
    EditText editTextFBName,editTextFBEmailId,editTextFBContact,edittext_FBcomment;
    Button btnFeedbacksubmit;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FeedbackFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
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
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_feedback, container, false);
        initview();

        btnFeedbacksubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextFBName.getText().toString().length()==0){

                    /*editTextFBName.requestFocus();
                    editTextFBName.setError("Please Enter Full Name");*/
                    Toast.makeText(getContext(),"Please enter Name",Toast.LENGTH_SHORT).show();

                } else if(editTextFBEmailId.getText().toString().length()==0){

                    Toast.makeText(getContext(),"Please enter Emai id",Toast.LENGTH_SHORT).show();
                }
                else if(editTextFBContact.getText().toString().length()==0){

                    Toast.makeText(getContext(),"Please enter contact",Toast.LENGTH_SHORT).show();

                } else if(edittext_FBcomment.getText().toString().length()==0){

                    Toast.makeText(getContext(),"Please enter Feedback",Toast.LENGTH_SHORT).show();

                }else {
                    executeFeedbackService();
                }
            }

        });

        return view;
    }

    private void executeFeedbackService() {


    }

    private void initview() {
        editTextFBName=(EditText) getView().findViewById(R.id.editTextFBName);
        editTextFBEmailId=(EditText) getView().findViewById(R.id.editTextFBEmailId);
        editTextFBContact=(EditText) getView().findViewById(R.id.editTextFBContact);
        edittext_FBcomment=(EditText) getView().findViewById(R.id.edittext_FBcomment);
        btnFeedbacksubmit=(Button) getView().findViewById(R.id.btnFeedbacksubmit);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
