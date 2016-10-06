package com.alsash.android.criminalintent.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alsash.android.criminalintent.R;
import com.alsash.android.criminalintent.data.Crime;
import com.alsash.android.criminalintent.data.CrimeLab;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CrimeAdapter mAdapter;

    private class CrimeHolder extends RecyclerView.ViewHolder {
        TextView mTitleTextView;

        CrimeHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.mTitleTextView.setText(crime.getTitle());
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }


    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.crime_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUi();

        return rootView;
    }

    private void updateUi() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mRecyclerView.setAdapter(mAdapter);
    }
}
