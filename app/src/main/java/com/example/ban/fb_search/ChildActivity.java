package com.example.ban.fb_search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ban.fb_search.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ChildActivity extends AppCompatActivity
        implements PageFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private String[] dataParam;
    private ViewPager viewPager;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fm = getSupportFragmentManager();
        viewPager.setAdapter(new SampleFragmentPagerAdapter(fm, ChildActivity.this));

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            dataParam = intentThatStartedThisActivity.getStringArrayExtra(Intent.EXTRA_TEXT);
        }

        //viewPager.setCurrentItem(Integer.parseInt(dataParam[6]));
        //TabLayout.Tab tab = tabLayout.getTabAt(Integer.parseInt(dataParam[6]));
        //tab.select();
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 5;
        private Context context;
        private int[] imageResId = {
                R.drawable.users,
                R.drawable.pages,
                R.drawable.events,
                R.drawable.places,
                R.drawable.groups
        };

        private String[] textResId = {"UUsers", "EPages", "EEvents", "EPlaces", "EGroups"};


        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            //mPosition = position;
            PageFragment fragment = PageFragment.newInstance(dataParam[position], position, dataParam[dataParam.length-2], dataParam);
            fragment.setTitle(dataParam[position]);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(textResId[position]);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }

        @Override
        public int getItemPosition(Object item) {
            PageFragment fragment = (PageFragment) item;
            String title = fragment.getTitle();
            int position = java.util.Arrays.asList(dataParam).indexOf(title);

            if (position >= 0) {
                return position;
            } else {
                return POSITION_NONE;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            List<Fragment> fragmentsList = fm.getFragments();
            if (fragmentsList != null && position <= (fragmentsList.size() - 1)) {
                PageFragment sampleFragment = (PageFragment) fragmentsList.get(position);
                String tmp = dataParam[position];
                //If the current data of the fragment changed, set the new data
                if (tmp != null && !tmp.equals(sampleFragment.getTitle())) {
                    sampleFragment.setTitle(tmp);
                    //Log.i(TAG, "********instantiateItem position:" + position + " FragmentDataChanged");
                } else {

                }
            } else {
                //No fragment instance available for this index, create a new fragment by calling getItem() and show the data.
                //Log.i(TAG, "********instantiateItem position:" + position + " NewFragmentCreated");
            }

            return super.instantiateItem(container, position);
        }
    }

    public void onDataReceived(String page) {
        makeSearchQuery(page);
    }

    private void makeSearchQuery(String query) {
        URL userUrl = NetworkUtils.buildUrl(query, "link", null, null);
        new queryTask().execute(userUrl);
    }

    public class queryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            //URL searchUrl = urls[0];
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
            if (searchResults != null && !searchResults.equals("")) {
                //mSearchResultsTextView.setText(searchResults[0]);
                int pos = viewPager.getCurrentItem();
                dataParam[pos] = searchResults;
                viewPager.getAdapter().notifyDataSetChanged();
            }
        }
    }
}
