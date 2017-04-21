package com.example.ban.fb_search;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecondFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String ARG_PARAM1 = "user";
    static final String ARG_PARAM2 = "page";
    static final String ARG_PARAM3 = "event";
    static final String ARG_PARAM4 = "place";
    static final String ARG_PARAM5 = "group";

    // TODO: Rename and change types of parameters
    private String userParam;
    private String pageParam;
    private String eventParam;
    private String placeParam;
    private String groupParam;

    private OnFragmentInteractionListener mListener;
    private TabLayout tabLayout;
    //private ViewPager viewPager;

    //private FragmentActivity myContext;

    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String[] param) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param[0]);
        args.putString(ARG_PARAM2, param[1]);
        args.putString(ARG_PARAM3, param[2]);
        args.putString(ARG_PARAM4, param[3]);
        args.putString(ARG_PARAM5, param[4]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*try {
                JSONObject[] data = new JSONObject[5];
                for (int i = 0; i < searchResults.length; i++) {
                    data[i] = new JSONObject(searchResults[i]);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            userParam = getArguments().getString(ARG_PARAM1);
            pageParam = getArguments().getString(ARG_PARAM2);
            eventParam = getArguments().getString(ARG_PARAM3);
            placeParam = getArguments().getString(ARG_PARAM4);
            groupParam = getArguments().getString(ARG_PARAM5);
        }

    }

    private void createTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Tab 1");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.users, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Tab 2");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.pages, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabThree.setText("Tab 3");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.events, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    public static class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[] { "Tab1", "Tab2", "tab3" };

        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            //this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {

            return PageFragment.newInstance(position + 1);
            //return mFragmentList.get(position);
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
            /*Drawable image = ContextCompat.getDrawable(getActivity(), imageResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
            //return mFragmentTitleList.get(position);
            //return sb;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        FragmentManager fm = getChildFragmentManager();
        viewPager.setAdapter(new SampleFragmentPagerAdapter(fm));
        
        /*SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(fm);
        adapter.addFrag(new Fragment_One(), "Tab 1");
        adapter.addFrag(new Fragment_Two(), "Tab 2");
        adapter.addFrag(new Fragment_Three(), "Tab 3");
        viewPager.setAdapter(adapter);*/

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
