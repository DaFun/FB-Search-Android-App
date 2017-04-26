package com.example.ban.fb_search;

/**
 * Created by Ban on 4/24/2017.
 */

import com.example.ban.fb_search.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData(String data) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        try {
            JSONObject obj = new JSONObject(data);
            if (obj.has("albums")) {
                JSONArray albums = obj.getJSONObject("albums").getJSONArray("data");
                for (int i = 0; i < albums.length(); i++) {
                    JSONObject tmp = albums.getJSONObject(i);
                    String name = tmp.getString("name");
                    JSONArray photoIds = tmp.getJSONObject("photos").getJSONArray("data");
                    List<String> list = new ArrayList<String>();
                    for (int j = 0; j < photoIds.length(); j++) {
                        list.add(buildUrl(photoIds.getJSONObject(j).getString("id")));
                    }
                    expandableListDetail.put(name, list);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return expandableListDetail;
    }

    private static String buildUrl(String id) {
        return "https://graph.facebook.com/v2.8/"+id+
                "/picture?access_token=EAADstsKuf0MBAO67Hm3DRV5bUx34NzfZAcrSPXid0Eky1ZAMin7YoamWnusmRj7mxF7Ns3J8Au1qNL11iDQCx7Fp4RFZADbXskaszqwCTXwmMQoIpO74FlODxtKPviXbUxHxKMnaJts0wihu8XKoj1bxlgjQfkZD";
    }
}