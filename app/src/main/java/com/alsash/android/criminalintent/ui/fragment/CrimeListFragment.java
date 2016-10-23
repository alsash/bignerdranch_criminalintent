package com.alsash.android.criminalintent.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alsash.android.criminalintent.R;
import com.alsash.android.criminalintent.data.Crime;
import com.alsash.android.criminalintent.data.CrimeLab;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private static final String ARG_SUBTITLE_VISIBLE = "subtitle_visible";

    private RecyclerView mRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mIsSubtitleVisible;
    private Callbacks mCallbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crime_list, container, false);

        if (savedInstanceState != null) {
            mIsSubtitleVisible = savedInstanceState.getBoolean(ARG_SUBTITLE_VISIBLE, false);
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.crime_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUi();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_SUBTITLE_VISIBLE, mIsSubtitleVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mIsSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                newCrime();
                return true;
            case R.id.menu_item_show_subtitle:
                mIsSubtitleVisible = !mIsSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void newCrime() {
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);
        updateUi();
        mCallbacks.onItemSelected(crime);
    }

    public void updateUi() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = null;
        if (mIsSubtitleVisible) {
            subtitle = getResources().getString(R.string.subtitle_format, crimeCount);
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            activity.getSupportActionBar().setSubtitle(subtitle);
        }
    }

    /**
     * Obligatory interface for host activities
     */
    public interface Callbacks {
        void onItemSelected(Crime crime);
    }

    private class EmptyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mEmptyListNotification;
        private Button mEmptyListButton;

        EmptyHolder(View itemView) {
            super((itemView));
            mEmptyListNotification = (TextView)
                    itemView.findViewById(R.id.list_item_empty_notification);
            mEmptyListButton = (Button)
                    itemView.findViewById(R.id.list_item_empty_button);
            mEmptyListButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            newCrime();
        }
    }
    private class CrimeHolder extends RecyclerView.ViewHolder
            implements
            View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        CrimeHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)
                    itemView.findViewById(R.id.list_item_crime_solved_checkbox);
            itemView.setOnClickListener(this);
        }

        void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onItemSelected(mCrime);
        }


    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int VH_TYPE_EMPTY = 1;
        private static final int VH_TYPE_CRIME = 2;

        private List<Crime> mCrimes;

        CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if (viewType == VH_TYPE_CRIME) {
                View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
                return new CrimeHolder(view);
            } else {     // VH_TYPE_EMPTY
                View view = layoutInflater.inflate(R.layout.list_item_empty, parent, false);
                return new EmptyHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CrimeHolder) {
                CrimeHolder crimeHolder = (CrimeHolder) holder;
                Crime crime = mCrimes.get(position);
                crimeHolder.bindCrime(crime);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mCrimes.size() == 0) {
                return VH_TYPE_EMPTY;
            } else {
                return VH_TYPE_CRIME;
            }
        }

        @Override
        public int getItemCount() {
            if (mCrimes.size() == 0) {
                return 1;
            } else {
                return mCrimes.size();
            }
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }
}
