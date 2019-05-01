package com.siwakorn.lifeiseasy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends Fragment {

    private JSONArray allJobs;
    private String filter = "All";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, null);
        Spinner jobSpinner = view.findViewById(R.id.jobSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.jobList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSpinner.setAdapter(adapter);
        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final RecyclerView jobListRecycler = view.findViewById(R.id.jobListRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        jobListRecycler.setLayoutManager(layoutManager);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        String ticket = sharedPreferences.getString("ticket", "");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = "http://54.179.153.2:9000/job?ticket=" + ticket;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        allJobs = response;
                        RecyclerView.Adapter recyclerAdapter = new MyAdapter(allJobs);
                        jobListRecycler.setAdapter(recyclerAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonArrayRequest);

        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filter.equals("All")) {
                    RecyclerView.Adapter recyclerAdapter = new MyAdapter(allJobs);
                    jobListRecycler.setAdapter(recyclerAdapter);
                } else {
                    JSONArray filteredJobs = new JSONArray();
                    for (int i = 0; i < allJobs.length(); i++) {
                        try {
                            if (allJobs.getJSONObject(i).getString("name").equals(filter)) {
                                filteredJobs.put(allJobs.getJSONObject(i));
                            }
                            RecyclerView.Adapter recyclerAdapter = new MyAdapter(filteredJobs);
                            jobListRecycler.setAdapter(recyclerAdapter);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });

        return view;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private JSONArray mDataset;

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

        public MyAdapter(JSONArray myDataset) {
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
            try {
                holder.name.setText(mDataset.getJSONObject(position).getString("provider"));
                holder.job.setText(mDataset.getJSONObject(position).getString("name"));
                holder.price.setText(mDataset.getJSONObject(position).getString("price"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'");
                Date date = simpleDateFormat.parse(mDataset.getJSONObject(position).getString("date"));
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                holder.date.setText(outputFormat.format(date));
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.length();
        }
    }

}