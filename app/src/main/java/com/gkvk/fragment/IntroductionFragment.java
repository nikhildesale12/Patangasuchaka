package com.gkvk.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import com.gkvk.R;
import com.gkvk.main.MainActivity;
import com.gkvk.util.ApplicationConstant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IntroductionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IntroductionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class IntroductionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button backFromView;
    private OnFragmentInteractionListener mListener;

    public IntroductionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static IntroductionFragment newInstance(String param1, String param2) {
        IntroductionFragment fragment = new IntroductionFragment();
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
        //View view = inflater.inflate(R.layout.fragment_introduction, container, false);

        //String introxml = getResources().getString(R.string.introduction);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(ApplicationConstant.MY_PREFS_NAME_INTRO, MODE_PRIVATE);
        String introduction = sharedPreferences.getString(ApplicationConstant.KEY_INTRO, "");

        View view= null;
        if(introduction != null && introduction.length()==0){
            view = inflater.inflate(R.layout.fragment_introduction_static, container, false);
        }else{
            view = inflater.inflate(R.layout.fragment_introduction, container, false);

            final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
            progressDialog.setMessage("Loading Data...");
            progressDialog.setCancelable(false);
            WebView web_view = view.findViewById(R.id.web_view);
            web_view.requestFocus();
            web_view.getSettings().setLightTouchEnabled(true);
            web_view.getSettings().setJavaScriptEnabled(true);
            web_view.getSettings().setGeolocationEnabled(true);
            web_view.setSoundEffectsEnabled(true);
            web_view.loadData("<html><body>"+introduction+"</body></html>",
                    "text/html", "UTF-8");
            web_view.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if (progress < 100) {
                        progressDialog.show();
                    }
                    if (progress == 100) {
                        progressDialog.dismiss();
                    }
                }
            });

            backFromView = (Button) view.findViewById(R.id.backFromView);
            backFromView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).setTitle("Home");
                    Fragment fragment = new HomeFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragment).commit();
                }
            });

        }

        /*FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        return view;
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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
