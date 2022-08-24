package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyFragmentPagerAdapter extends FragmentStateAdapter {

    private String ticker;
    private long current_timestamp;

    public MyFragmentPagerAdapter( FragmentActivity fragmentActivity, String ticker, long current_timestamp) {
        super(fragmentActivity);
        this.ticker = ticker;
        this.current_timestamp = current_timestamp;
    }
    public MyFragmentPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    public MyFragmentPagerAdapter(FragmentManager fragmentManager,  Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment_1 = new FirstFragment(ticker, current_timestamp);;
        Fragment fragment_2 = new SecondFragment(ticker);;

//        fragment = new FirstFragment();
        if (position == 0) {
            return fragment_1;
        }
        if (position == 1) {
            return fragment_2;
        }
        return fragment_1;
        // Return a NEW fragment instance in createFragment(int)
//        Fragment fragment = new FirstFragment();
//        Bundle args = new Bundle();
        // Our object is just an integer :-P
//        args.putInt(FirstFragment.ARG_OBJECT, position + 1);
//        fragment.setArguments(args);
//        return fragment_1;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}