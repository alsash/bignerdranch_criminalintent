package com.alsash.android.criminalintent.ui.activity;

import android.support.v4.app.Fragment;

import com.alsash.android.criminalintent.ui.fragment.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

}
