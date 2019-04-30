package com.siwakorn.lifeiseasy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, null);

        RecyclerView jobListRecycler = view.findViewById(R.id.jobListRecycler);
        jobListRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        jobListRecycler.setLayoutManager(layoutManager);
        List<List<String>> initialData = new ArrayList<>();
        List<String> data = new ArrayList<>();
        data.add("Application");
        data.add("500");
        data.add("16/08/2019");
        for (int i = 0; i < 10; i++) {
            initialData.add(data);
        }
        RecyclerView.Adapter jobAdapter = new jobAdapter(initialData);
        jobListRecycler.setAdapter(jobAdapter);

        RecyclerView employmentRecycler = view.findViewById(R.id.employmentRecycler);
        employmentRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        employmentRecycler.setLayoutManager(layoutManager1);
        List<List<String>> initialData2 = new ArrayList<>();
        List<String> data2 = new ArrayList<>();
        data2.add("Somchai Hahaha");
        data2.add("Application");
        data2.add("500");
        data2.add("16/08/2019");
        for (int i = 0; i < 10; i++) {
            initialData2.add(data2);
        }
        RecyclerView.Adapter employmentAdapter = new employmentAdapter(initialData2);
        employmentRecycler.setAdapter(employmentAdapter);

        return view;
    }

    public class jobAdapter extends RecyclerView.Adapter<jobAdapter.MyViewHolder> {
        private List<List<String>> mDataset;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, job, price, date;

            public MyViewHolder(View v) {
                super(v);
                job = v.findViewById(R.id.job);
                price = v.findViewById(R.id.price);
                date = v.findViewById(R.id.date);
            }
        }

        public jobAdapter(List<List<String>> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public jobAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.job_item_profile, parent, false);
            jobAdapter.MyViewHolder vh = new jobAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(jobAdapter.MyViewHolder holder, int position) {
            holder.job.setText(mDataset.get(position).get(0));
            holder.price.setText(mDataset.get(position).get(1));
            holder.date.setText(mDataset.get(position).get(2));
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    public class employmentAdapter extends RecyclerView.Adapter<employmentAdapter.MyViewHolder> {
        private List<List<String>> mDataset;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, job, price, date;

            public MyViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.name);
                job = v.findViewById(R.id.job);
                price = v.findViewById(R.id.price);
                date = v.findViewById(R.id.date);
            }
        }

        public employmentAdapter(List<List<String>> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public employmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.employment_item_profile, parent, false);
            employmentAdapter.MyViewHolder vh = new employmentAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(employmentAdapter.MyViewHolder holder, int position) {
            holder.name.setText(mDataset.get(position).get(0));
            holder.job.setText(mDataset.get(position).get(1));
            holder.price.setText(mDataset.get(position).get(2));
            holder.date.setText(mDataset.get(position).get(3));
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

}