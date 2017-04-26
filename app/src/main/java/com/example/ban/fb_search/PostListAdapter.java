package com.example.ban.fb_search;

/**
 * Created by Ban on 4/24/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ban.fb_search.utilities.ImageLoader;

import java.util.ArrayList;


/**
 * Created by Ban on 4/22/2017.
 */

public class PostListAdapter extends ArrayAdapter<PostItem> {
    private ArrayList<PostItem> items;
    private int layoutResourceId;
    private Context context;

    public PostListAdapter(Context context, int layout, ArrayList<PostItem> items) {
        super(context, layout, items);
        this.layoutResourceId = layout;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);
        }

        PostItem item = items.get(position);
        ImageView img = (ImageView) row.findViewById(R.id.post_image);
        TextView name = (TextView) row.findViewById(R.id.post_name);
        TextView time = (TextView) row.findViewById(R.id.post_time);
        TextView content = (TextView) row.findViewById(R.id.post_content);

        setupItem(item, img, name, time, content);
        return row;
    }

    private void setupItem(PostItem item, ImageView img, TextView name, TextView time, TextView content) {
        new ImageLoader(item.url, img).execute();
        name.setText(item.name);
        time.setText(item.time);
        content.setText(item.content);
    }
}
