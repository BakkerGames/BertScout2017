package com.bertrobotics.bertscout2017;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                StandScouting standScoutingTab = new StandScouting();
                return standScoutingTab;
            case 1:
                PitScouting pitScoutingTab = new PitScouting();
                return pitScoutingTab;
            case 2:
                Statistics statisticsTab = new Statistics();
                return statisticsTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}