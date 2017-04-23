package com.example.ban.fb_search;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.content.Context;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.content.SharedPreferences;
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

    public ProfileArrayAdapter(Context context, int layout, ArrayList<ProfileItem> items, String t, SharedPreferences id, SharedPreferences type) {
        super(context, layout, items);
        this.layoutResourceId = layout;
        this.context = context;
        this.items = items;
        this.sharedPreferencesId = id;
        this.sharedPreferencesType = type;
        mType = t;
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
        new ImageLoadTask(holder.ProfileItem.url, holder.photo).execute();

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
                Context context = getContext();

                Class destinationActivity = DetailActivity.class;

                Intent startDetailActivityIntent = new Intent(context, destinationActivity);
                startDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, holder.ProfileItem.data);

                getContext().startActivity(startDetailActivityIntent);
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

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }

}
