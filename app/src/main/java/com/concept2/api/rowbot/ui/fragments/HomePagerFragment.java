package com.concept2.api.rowbot.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.concept2.api.rowbot.R;

public class HomePagerFragment extends BaseFragment {

    private ViewPager mPager;
    private BasePageFragment[] mFragments;

    private PagerAdapter mPagerAdapter;
    private int mPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragments = new BasePageFragment[] {
                new HomeFragment(), new NewWorkoutFragment()
        };
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        int currentItem = mPager.getCurrentItem();
        mPager.setCurrentItem(currentItem);
        mParent.getSupportActionBar().setTitle("RowBot");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mPagerAdapter = new PagerAdapter(getChildFragmentManager());
        View root = inflater.inflate(R.layout.home_pager_fragment, null);
        mPager = (ViewPager) root.findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(mPagerAdapter);
        mPager.setCurrentItem(mPosition);
        ((PagerSlidingTabStrip) root.findViewById(R.id.tabs)).setViewPager(mPager);
        return root;
    }

    private final class PagerAdapter extends FragmentPagerAdapter implements
            ViewPager.OnPageChangeListener {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments[position].getPageTitle();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
