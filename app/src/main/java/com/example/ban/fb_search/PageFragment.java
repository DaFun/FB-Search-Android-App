package com.example.ban.fb_search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ban on 4/18/2017.
 */

public class PageFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_TYPE = "ARG_TYPE";
    public static final String ARG_FRAGMENT = "ARG_FRAGMENT";

    private String mPage;
    SharedPreferences sharedPreferencesId;
    SharedPreferences sharedPreferencesType;
    private String mType;

    private String mPrevious;
    private boolean mDisable_prev;
    private String mNext;
    private boolean mDisable_next;
    private String mFragment;
    private String[] mArray;

    private OnFragmentInteractionListener mListener;

    private Button previous;
    private Button next;
    private LinearLayout bottom_bt;

    public static PageFragment newInstance(String data, int index, String fragment_type, String[] array) {
        Bundle args = new Bundle();
        String type;
        switch (index) {
            case 0:
                type = "user";
                break;
            case 1:
                type = "page";
                break;
            case 2:
                type = "event";
                break;
            case 3:
                type = "place";
                break;
            default:
                type = "group";
        }
        args.putString(ARG_PAGE, data);
        args.putString(ARG_TYPE, type);
        args.putString(ARG_FRAGMENT, fragment_type);
        args.putStringArray("array", array);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getString(ARG_PAGE);
        mType = getArguments().getString(ARG_TYPE);
        mFragment = getArguments().getString(ARG_FRAGMENT);
        mArray = getArguments().getStringArray("array");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        sharedPreferencesId = getActivity().getSharedPreferences("profile_id", Context.MODE_PRIVATE);
        sharedPreferencesType = getActivity().getSharedPreferences("type", Context.MODE_PRIVATE);

        ProfileArrayAdapter adapter;
        if (mFragment.equals("first")) {
            adapter = new ProfileArrayAdapter
                    (getActivity(), R.layout.number_list_item, buildProfileList(mPage, sharedPreferencesId),
                            mType, sharedPreferencesId, sharedPreferencesType, mArray);
        } else {
            adapter = new ProfileArrayAdapter
                    (getActivity(), R.layout.number_list_item, buildProfileList(),
                            mType, sharedPreferencesId, sharedPreferencesType, mArray);
        }
        ListView profileListView = (ListView) view.findViewById(R.id.rv_numbers);
        profileListView.setAdapter(adapter);

        previous = (Button) view.findViewById(R.id.bt_previous);
        next = (Button) view.findViewById(R.id.bt_next);
        bottom_bt = (LinearLayout) view.findViewById(R.id.bottom_bt);
        parsePagination(mPage);

        previous.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!mDisable_prev) {
                    mListener.onDataReceived(mPrevious);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!mDisable_next) {
                    mListener.onDataReceived(mNext);
                }
            }
        });

        return view;
    }

    public static ArrayList<ProfileItem> buildProfileList(String data, SharedPreferences sharedPreferencesId) {
        try {
            JSONObject mJSONItems = new JSONObject(data);
            JSONArray mDataArray = mJSONItems.getJSONArray("data");
            ArrayList<ProfileItem> profileList = new ArrayList<>();

            for (int i = 0; i < mDataArray.length(); i++) {
                JSONObject obj = mDataArray.getJSONObject(i);
                String name = obj.getString("name");
                String id = obj.getString("id");
                String url = obj.getJSONObject("picture").getJSONObject("data").getString("url");
                boolean favorite = sharedPreferencesId.contains(id);
                profileList.add(new ProfileItem(name, url, favorite, id, obj.toString()));
            }
            return profileList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ArrayList<ProfileItem> buildProfileList() {
        ArrayList<ProfileItem> result = new ArrayList<>();

        Set<String> tmp = new HashSet<String>();
        Set<String> s = sharedPreferencesType.getStringSet(mType, tmp);
        for (String item : s) {
            String data = sharedPreferencesId.getString(item, null);
            if (data != null) {
                try {
                    JSONObject obj = new JSONObject(data);
                    String name = obj.getString("name");
                    String url = obj.getJSONObject("picture").getJSONObject("data").getString("url");
                    result.add(new ProfileItem(name, url, true, item, data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public void parsePagination(String data) {
        if (data != null && data != "") {
            try {
                JSONObject obj = new JSONObject(data);
                JSONObject page = obj.getJSONObject("paging");
                if (page.has("previous")) {
                    previous.setEnabled(true);
                    mPrevious = page.getString("previous");
                    mDisable_prev = false;
                } else {
                    previous.setEnabled(false);
                    mDisable_prev = true;
                }
                if (page.has("next")) {
                    next.setEnabled(true);
                    mNext = page.getString("next");
                    mDisable_next = false;
                } else {
                    next.setEnabled(false);
                    mDisable_next = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            bottom_bt.setVisibility(View.GONE);
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

    public interface OnFragmentInteractionListener {
        public void onDataReceived(String data);
    }

    public String getTitle() {
        return mPage;
    }

    public void setTitle(String title) {
        mPage = title;
    }
}
