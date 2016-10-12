package com.alsash.android.criminalintent.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.alsash.android.criminalintent.ui.fragment.DatePickerFragment;

import java.util.Date;

public class DatePickerActivity extends SingleFragmentActivity {

    private static final String EXTRA_DATE = "com.alsash.android.criminalintent.date";

    public static Intent newIntent(Context context, Date date) {
        Intent intent = new Intent(context, DatePickerActivity.class);
        intent.putExtra(EXTRA_DATE, date);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Date date = (Date) getIntent().getSerializableExtra(EXTRA_DATE);
        return DatePickerFragment.newInstance(date);
    }
}
