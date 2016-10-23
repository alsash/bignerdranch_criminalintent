package com.alsash.android.criminalintent.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.alsash.android.criminalintent.R;
import com.alsash.android.criminalintent.data.Crime;
import com.alsash.android.criminalintent.ui.fragment.CrimeFragment;
import com.alsash.android.criminalintent.ui.fragment.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity
        implements
        CrimeListFragment.Callbacks,
        CrimeFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_master_detail;
    }

    @Override
    public void onItemSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            Fragment detail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, detail)
                    .commit();
        }
    }
    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment crimeListFragment = (CrimeListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUi();
    }
}
