package com.example.ban.fb_search;

/**
 * Created by Ban on 4/22/2017.
 */

public class ProfileItem {
    public String name;
    public String url;
    public boolean favorite;
    public String id;
    public String data;

    public ProfileItem(String name, String url, boolean favorite, String id, String data) {
        this.name = name;
        this.url = url;
        this.favorite = favorite;
        this.id = id;
        this.data = data;
    }
}
