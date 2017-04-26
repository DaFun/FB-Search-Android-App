package com.example.ban.fb_search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ban on 4/25/2017.
 */

public class PostListDataPump {
    public static ArrayList<PostItem> getData(String[] data) {
        try {
            JSONObject obj = new JSONObject(data[0]);
            JSONArray mDataArray = obj.getJSONObject("posts").getJSONArray("data");
            ArrayList<PostItem> postList = new ArrayList<>();

            for (int i = 0; i < mDataArray.length(); i++) {
                JSONObject tmp = mDataArray.getJSONObject(i);
                String message = tmp.getString("message");
                String time = tmp.getString("created_time");
                time = time.replace("T", " ").substring(0, 19);
                postList.add(new PostItem(data[1], time, message, data[2]));
            }
            return postList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
