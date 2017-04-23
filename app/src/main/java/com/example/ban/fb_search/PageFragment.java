package com.example.ban.fb_search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ban on 4/18/2017.
 */

public class PageFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_TYPE = "ARG_TYPE";

    private String mPage;
    SharedPreferences sharedPreferencesId;
    SharedPreferences sharedPreferencesType;
    private String mType;

    public static PageFragment newInstance(String data, int index) {
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
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getString(ARG_PAGE);
        mType = getArguments().getString(ARG_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        sharedPreferencesId = getActivity().getSharedPreferences("profile_id", Context.MODE_PRIVATE);
        sharedPreferencesType = getActivity().getSharedPreferences("type", Context.MODE_PRIVATE);

        ProfileArrayAdapter adapter = new ProfileArrayAdapter
                (getActivity(), R.layout.number_list_item, buildProfileList(mPage, sharedPreferencesId),
                        mType, sharedPreferencesId, sharedPreferencesType);
        ListView profileListView = (ListView) view.findViewById(R.id.rv_numbers);
        profileListView.setAdapter(adapter);

        /*mRecordList = (RecyclerView) view.findViewById(R.id.rv_numbers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecordList.setLayoutManager(layoutManager);
        mRecordList.setHasFixedSize(true);
        mRecordList.setItemViewCacheSize(20);
        mRecordList.setDrawingCacheEnabled(true);
        mRecordList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mAdapter = new RVAdapter(mPage, getActivity());

        mRecordList.setAdapter(mAdapter);*/
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
                profileList.add(new ProfileItem(name, url, favorite, i, id, obj.toString()));
            }
            return profileList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}