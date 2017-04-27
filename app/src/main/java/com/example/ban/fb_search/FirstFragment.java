package com.example.ban.fb_search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FirstFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    private static final String ARG_PARAM1 = "lat";
    private static final String ARG_PARAM2 = "lon";

    private String mLat;
    private String mLon;

    private EditText mSearchBoxEditText;
    private ProgressBar mLoadingIndicator;

    private OnFragmentInteractionListener mListener;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
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
            mLat = getArguments().getString(ARG_PARAM1);
            mLon = getArguments().getString(ARG_PARAM2);
        }

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        mSearchBoxEditText = (EditText) view.findViewById(R.id.et_search_box);
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);

        final Button button = (Button) view.findViewById(R.id.bt_search);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String query = mSearchBoxEditText.getText().toString();
                if (query == null || query.equals("")) {
                    Toast.makeText(getActivity(), "Please input keywords", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.onDataReceived(query, mLoadingIndicator);
                }
            }
        });

        final Button button_cl = (Button) view.findViewById(R.id.bt_clear);
        button_cl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mSearchBoxEditText.setText("");
            }
        });

        mSearchBoxEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(v);
            }
            }
        });

        return view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onDataReceived(String data, ProgressBar mLoadingIndicator);
    }
}
