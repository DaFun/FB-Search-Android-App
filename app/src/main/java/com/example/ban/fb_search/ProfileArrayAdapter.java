package com.example.ban.fb_search;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.content.Context;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ban.fb_search.utilities.NetworkUtils;
import com.example.ban.fb_search.utilities.ImageLoader;
import java.util.Set;


/**
 * Created by Ban on 4/22/2017.
 */

public class ProfileArrayAdapter extends ArrayAdapter<ProfileItem> {
    private ArrayList<ProfileItem> items;
    private int layoutResourceId;
    private Context context;
    SharedPreferences sharedPreferencesId;
    SharedPreferences sharedPreferencesType;
    private String mType;
    private String[] mArray;

    public ProfileArrayAdapter(Context context, int layout, ArrayList<ProfileItem> items,
                               String t, SharedPreferences id, SharedPreferences type, String[] array) {
        super(context, layout, items);
        this.layoutResourceId = layout;
        this.context = context;
        this.items = items;
        this.sharedPreferencesId = id;
        this.sharedPreferencesType = type;
        mType = t;
        mArray = array;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ProfileItemHolder holder = null;

        if (convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);
        }

        holder = new ProfileItemHolder();
        holder.ProfileItem = items.get(position);
        holder.favorite = (ImageView) row.findViewById(R.id.favorite_img);
        holder.favorite.setTag(position);

        holder.name = (TextView)row.findViewById(R.id.tv_item_name);
        holder.photo = (ImageView) row.findViewById(R.id.profile_img);
        holder.details = (ImageView) row.findViewById(R.id.details_img);
        holder.details.setTag(position);

        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(final ProfileItemHolder holder) {
        holder.name.setText(holder.ProfileItem.name);
        new ImageLoader(holder.ProfileItem.url, holder.photo).execute();

        if (holder.ProfileItem.favorite) {
            holder.favorite.setImageResource(R.drawable.favorites_on);
        } else {
            holder.favorite.setImageResource(R.drawable.favorites_off);
        }

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                if (sharedPreferencesId.contains(holder.ProfileItem.id)) {
                    holder.favorite.setImageResource(R.drawable.favorites_off);

                    SharedPreferences.Editor editorId = sharedPreferencesId.edit();
                    editorId.remove(holder.ProfileItem.id);
                    editorId.commit();

                    Set<String> h = new HashSet<String>();
                    Set<String> tmp = sharedPreferencesType.getStringSet(mType, h);
                    if (tmp.contains(holder.ProfileItem.id)) {
                        tmp.remove(holder.ProfileItem.id);
                    }
                    SharedPreferences.Editor editorType = sharedPreferencesType.edit();
                    editorType.putStringSet(mType, tmp);
                    editorType.commit();

                } else {
                    holder.favorite.setImageResource(R.drawable.favorites_on);

                    SharedPreferences.Editor editorId = sharedPreferencesId.edit();
                    editorId.putString(holder.ProfileItem.id, holder.ProfileItem.data);
                    editorId.commit();

                    Set<String> s = new HashSet<String>();
                    Set<String> tmp = sharedPreferencesType.getStringSet(mType, s);
                    tmp.add(holder.ProfileItem.id);
                    SharedPreferences.Editor editorType = sharedPreferencesType.edit();
                    editorType.putStringSet(mType, tmp);
                    editorType.commit();
                }
            }
        });

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            int position = (Integer) view.getTag();
            String[] bundle = {holder.ProfileItem.id, holder.ProfileItem.name,
                    holder.ProfileItem.url, holder.ProfileItem.data};
            makeSearchQuery(bundle);
            }
        });
    }

    public static class ProfileItemHolder {
        ProfileItem ProfileItem;
        TextView name;
        ImageView photo;
        ImageView favorite;
        ImageView details;
    }

    private void makeSearchQuery(String[] bundle) {
        URL idUrl = NetworkUtils.buildUrl(bundle[0], "id", null, null);
        new queryTask(bundle).execute(idUrl);
    }

    public class queryTask extends AsyncTask<URL, Void, String> {

        private String[] bundle;

        public queryTask(String[] bundle) {
            this.bundle = bundle;
        }

        @Override
        protected String doInBackground(URL... urls) {
            String searchResults = new String();
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {

            String favorite = sharedPreferencesId.contains(bundle[0]) ? "true" : "false";

            //name, url, id, data
            String[] tmp = {searchResults, bundle[1], bundle[2], bundle[0], bundle[3], mType,
                    mArray[0], mArray[1], mArray[2], mArray[3], mArray[4], mArray[5], favorite};

            Context context = getContext();
            Class destinationActivity = DetailActivity.class;

            Intent startDetailActivityIntent = new Intent(context, destinationActivity);
            startDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, tmp);
            context.startActivity(startDetailActivityIntent);

        }
    }
}
