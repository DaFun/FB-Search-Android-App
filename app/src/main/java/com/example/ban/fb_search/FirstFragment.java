package com.example.ban.fb_search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;
import android.view.inputmethod.InputMethodManager;

import com.example.ban.fb_search.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import android.support.v4.app.FragmentTransaction;

import org.json.JSONObject;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FirstFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText mSearchBoxEditText;
    private TextView mSearchResultsTextView;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private void makeSearchQuery() {
        String query = mSearchBoxEditText.getText().toString();
        URL userUrl = NetworkUtils.buildUrl(query, "user");
        URL pageUrl = NetworkUtils.buildUrl(query, "page");
        URL eventUrl = NetworkUtils.buildUrl(query, "event");
        URL placeUrl = NetworkUtils.buildUrl(query, "place");
        URL groupUrl = NetworkUtils.buildUrl(query, "group");


        //mUrlDisplayTextView.setText(githubSearchUrl.toString());
        new queryTask().execute(userUrl, pageUrl, eventUrl, placeUrl, groupUrl);
    }

    public class queryTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected String[] doInBackground(URL... urls) {
            //URL searchUrl = urls[0];
            String[] searchResults = new String[5];
            try {
                searchResults[0] = NetworkUtils.getResponseFromHttpUrl(urls[0]);
                searchResults[1] = NetworkUtils.getResponseFromHttpUrl(urls[1]);
                searchResults[2] = NetworkUtils.getResponseFromHttpUrl(urls[2]);
                searchResults[3] = NetworkUtils.getResponseFromHttpUrl(urls[3]);
                searchResults[4] = NetworkUtils.getResponseFromHttpUrl(urls[4]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String[] searchResults) {
            if (searchResults != null && !searchResults.equals("")) {
                mSearchResultsTextView.setText(searchResults[0]);

                mListener.onDataReceived(searchResults);
            }
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
        mSearchResultsTextView = (TextView) view.findViewById(R.id.tv_github_search_results_json);

        final Button button = (Button) view.findViewById(R.id.bt_search);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                makeSearchQuery();
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

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

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
        // TODO: Update argument type and name
        public void onDataReceived(String[] data);
    }
}
