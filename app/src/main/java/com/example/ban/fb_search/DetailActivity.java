package com.example.ban.fb_search;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    
    private ViewPager mViewPager;
    private String[] mData;
    private TabLayout tabLayout;

    private boolean if_album_error;
    private boolean if_post_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mData = intentThatStartedThisActivity.getStringArrayExtra(Intent.EXTRA_TEXT);
            }
        }
        loadDetailData();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewpager_detail);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_detail_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        createTabIcons();

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "Tab1", "Tab2" };


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            //this.context = context;
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
            return tabTitles[position];
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

}
