package gbc.sa.vansales.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import gbc.sa.vansales.Fragment.AllCustomerFragment;
import gbc.sa.vansales.Fragment.BeginDayFragment;
import gbc.sa.vansales.Fragment.MessageFragment;
import gbc.sa.vansales.Fragment.VisitAllFragment;
import gbc.sa.vansales.activities.BeginDay;

/**
 * Created by eheuristic on 12/2/2016.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String activity;

    public PagerAdapter(FragmentManager fm, int NumOfTabs,String activtyname) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        activity=activtyname;
    }

    @Override
    public Fragment getItem(int position) {



        if(activity.equals("b")) {
            switch (position) {
                case 0:
                    BeginDayFragment tab1 = new BeginDayFragment();
                    return tab1;
                case 1:
                    MessageFragment tab2 = new MessageFragment();
                    return tab2;

                default:
                    return null;
            }
        }
        else if(activity.equals("s"))
        {
            switch (position) {
                case 0:
                    VisitAllFragment tab1 = new VisitAllFragment();
                    return tab1;
                case 1:
                    AllCustomerFragment tab2 = new AllCustomerFragment();
                    return tab2;

                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}