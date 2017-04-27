package com.example.ban.fb_search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    
    private ViewPager mViewPager;
    private String[] mData;
    private TabLayout tabLayout;

    private boolean if_album_error;
    private boolean if_post_error;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Bitmap mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mData = intentThatStartedThisActivity.getStringArrayExtra(Intent.EXTRA_TEXT);
            }
        }
        new ImageLoader(mData[2]).execute();

        String type = mData[5];
        switch (type) {
            case "user":
                type = "0";
                break;
            case "page":
                type = "1";
                break;
            case "event":
                type = "2";
                break;
            case "place":
                type = "3";
                break;
            default:
                type = "4";
        }

        final String[] sendData = new String[7];
        for (int i = 0; i < 5; i++) {
            sendData[i] = mData[i + 6];
        }
        sendData[6] = type;
        sendData[5] = mData[mData.length - 2];

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                Context context = v.getContext();

                Class destinationActivity = ChildActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, sendData);

                startActivity(startChildActivityIntent);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getApplicationContext(), "Shared", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
            }
        });


        loadDetailData();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), DetailActivity.this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewpager_detail);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_detail_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        createTabIcons();

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String[] textResId = { "AAlbums", "PPosts" };
        private Context context;
        private int[] imageResId = {
                R.drawable.albums,
                R.drawable.posts
        };


        public SectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Album_Fragment.newInstance(if_album_error, mData[0]);
                default:
                    return Post_Fragment.newInstance(if_post_error, mData);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            /*Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(textResId[position]);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
            return textResId[position];
        }
    }

    private void createTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Albums");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.albums, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Posts");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.posts, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

    }

    private void loadDetailData() {
        JSONObject obj;
        if (mData[0] != null && !mData[0].equals("")) {
            try {
                obj = new JSONObject(mData[0]);
                if (obj.has("albums")) {
                    if_album_error = false;
                } else {
                    if_album_error = true;
                }
                if (obj.has("posts")) {
                    if_post_error = false;
                } else {
                    if_post_error = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if_album_error = true;
            if_post_error = true;
        }
    }

    MenuItem on;
    MenuItem off;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem on = (MenuItem) menu.findItem(R.id.action_favorite);
        MenuItem off = (MenuItem) menu.findItem(R.id.action_favorite_off);
        String favorite = mData[mData.length - 1];
        if (favorite.equals("true")) {
            on.setVisible(false);
            off.setVisible(true);
        } else {
            on.setVisible(true);
            off.setVisible(false);
        }
        this.on = on;
        this.off = off;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        SharedPreferences sharedPreferencesId = getSharedPreferences("profile_id", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferencesType = getSharedPreferences("type", Context.MODE_PRIVATE);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            String mType = mData[5];
            String mId = mData[3];
            String data = mData[4];
            if (!sharedPreferencesId.contains(mId)) {
                SharedPreferences.Editor editorId = sharedPreferencesId.edit();
                editorId.putString(mId, data);
                editorId.commit();

                Set<String> s = new HashSet<String>();
                Set<String> tmp = sharedPreferencesType.getStringSet(mType, s);
                tmp.add(mId);
                SharedPreferences.Editor editorType = sharedPreferencesType.edit();
                editorType.putStringSet(mType, tmp);
                editorType.commit();
                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                on.setVisible(false);
                off.setVisible(true);
            }
            return true;
        } else if (id == R.id.action_favorite_off) {
            String mType = mData[5];
            String mId = mData[3];
            String data = mData[4];
            if (sharedPreferencesId.contains(mId)) {
                SharedPreferences.Editor editorId = sharedPreferencesId.edit();
                editorId.remove(mId);
                editorId.commit();

                Set<String> h = new HashSet<String>();
                Set<String> tmp = sharedPreferencesType.getStringSet(mType, h);
                if (tmp.contains(mId)) {
                    tmp.remove(mId);
                }
                SharedPreferences.Editor editorType = sharedPreferencesType.edit();
                editorType.putStringSet(mType, tmp);
                editorType.commit();
                Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                on.setVisible(true);
                off.setVisible(false);
            }
            return true;
        } else if (id == R.id.action_share) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://developers.facebook.com"))
                    .setContentTitle(mData[1])
                    .setImageUrl(Uri.parse(mData[2]))
                    .setContentDescription("FB SEARCH FROM USC CSCI571")
                    .build();

            shareDialog.show(content);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoader(String url) {
            this.url = url;
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
            mImg = result;
        }
    }
}
