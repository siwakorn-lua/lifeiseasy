package com.siwakorn.lifeiseasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, null);
        Spinner jobSpinner = view.findViewById(R.id.jobSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.jobList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSpinner.setAdapter(adapter);
        RecyclerView jobListRecycler = view.findViewById(R.id.jobListRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        jobListRecycler.setLayoutManager(layoutManager);
        List<List<String>> initialData = new ArrayList<>();
        List<String> data = new ArrayList<>();
        data.add("Somchai Hahaha");
        data.add("Application");
        data.add("500");
        data.add("16/08/2019");
        for (int i = 0; i < 10; i++) {
            initialData.add(data);
        }
        RecyclerView.Adapter recyclerAdapter = new MyAdapter(initialData);
        jobListRecycler.setAdapter(recyclerAdapter);
        return view;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<List<String>> mDataset;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, job, price, date;

            public MyViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.name);
                job = v.findViewById(R.id.job);
                price = v.findViewById(R.id.price);
                date = v.findViewById(R.id.date);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), EmployActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }

        public MyAdapter(List<List<String>> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.job_item_home, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
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