package com.grabber.pocapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.grabber.pocapp.fragment.HistoryFragment;
import com.grabber.pocapp.fragment.PropertyFragment;

import java.util.ArrayList;

public class VPAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> items;

    public VPAdapter(FragmentManager fm) {
        // noinspection deprecation
        super(fm);
        items = new ArrayList<>();
        items.add(PropertyFragment.newInstance());
        items.add(HistoryFragment.newInstance());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}