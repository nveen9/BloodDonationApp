package com.example.blooddonation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RegistrationAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public RegistrationAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                DonorTabFragment donorTabFragment = new DonorTabFragment();
                return donorTabFragment;
            case 1:
                DonnieTabFragment donnieTabFragment = new DonnieTabFragment();
                return donnieTabFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
